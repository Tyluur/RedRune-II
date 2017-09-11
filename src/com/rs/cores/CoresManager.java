package com.rs.cores;

import com.alex.store.Index;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.factory.DecoderThreadFactory;
import com.rs.cores.factory.SlowThreadFactory;
import com.rs.cores.schedule.Scheduler;
import com.rs.cores.thread.WorldThread;
import com.rs.game.GameConstants;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.link.OwnedObjectManager;
import com.rs.game.world.World;
import com.rs.game.world.region.Region;
import com.rs.game.world.region.RegionManager;
import com.rs.networking.ServerChannelHandler;
import com.rs.utility.Misc;
import com.rs.utility.constants.SkillConstants;
import com.rs.utility.game.files.SerializableFilesManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class CoresManager {
	
	public static ExecutorService serverWorkerChannelExecutor;
	
	public static ExecutorService serverBossChannelExecutor;
	
	public static Scheduler scheduler;
	
	public static Timer fastExecutor;
	
	public static ScheduledExecutorService slowExecutor;
	
	public static int serverWorkersCount;
	
	public static volatile boolean shutdown;
	
	public static long shutdownStart;
	
	public static int shutdownDelay;
	
	private static WorldThread worldThread;
	
	private static boolean checkAgility;
	
	private CoresManager() {
	
	}
	
	public static void init() {
		scheduler = new Scheduler();
		worldThread = new WorldThread();
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		serverWorkersCount = availableProcessors >= 6 ? availableProcessors - (availableProcessors >= 12 ? 7 : 5) : 1;
		serverWorkerChannelExecutor = availableProcessors >= 6 ? Executors.newFixedThreadPool(availableProcessors - (availableProcessors >= 12 ? 7 : 5), new DecoderThreadFactory()) : Executors.newSingleThreadExecutor(new DecoderThreadFactory());
		serverBossChannelExecutor = Executors.newSingleThreadExecutor(new DecoderThreadFactory());
		fastExecutor = new Timer("Fast Executor");
		slowExecutor = availableProcessors >= 6 ? Executors.newScheduledThreadPool(availableProcessors >= 12 ? 4 : 2, new SlowThreadFactory()) : Executors.newSingleThreadScheduledExecutor(new SlowThreadFactory());
		worldThread.start();
		registerTasks();
	}
	
	/**
	 * Registers the scheduled tasks
	 */
	private static void registerTasks() {
		addAccountsSavingTask();
		addCleanMemoryTask();
		addRestoreRunEnergyTask();
		addRestoreHitPointsTask();
		addRestoreSkillsTask();
		addRestoreSpecialAttackTask();
		addSummoningEffectTask();
		addOwnedObjectsTask();
	}
	
	private static void addAccountsSavingTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(() -> {
			try {
				saveFiles();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
		}, 1, 1, TimeUnit.SECONDS);
	}
	
	private static void addCleanMemoryTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(() -> {
			try {
				cleanMemory(Runtime.getRuntime().freeMemory() < GameConstants.MIN_FREE_MEM_ALLOWED);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}, 0, 10, TimeUnit.MINUTES);
	}
	
	private static void addRestoreRunEnergyTask() {
		fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : World.getPlayers()) {
						if (player == null || player.isDead() || !player.isRunning() || (checkAgility && player.getSkills().getLevel(SkillConstants.AGILITY) < 70)) {
							continue;
						}
						player.restoreRunEnergy();
					}
					checkAgility = !checkAgility;
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);
	}
	
	private static void addRestoreHitPointsTask() {
		fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : World.getPlayers()) {
						if (player == null || player.isDead() || !player.isRunning()) {
							continue;
						}
						player.restoreHitPoints();
					}
					for (NPC npc : World.getNPCs()) {
						if (npc == null || npc.isDead() || npc.hasFinished()) {
							continue;
						}
						npc.restoreHitPoints();
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}, 0, 6000);
	}
	
	private static void addRestoreSkillsTask() {
		fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : World.getPlayers()) {
						if (player == null || !player.isRunning()) {
							continue;
						}
						int ammountTimes = player.getPrayer().usingPrayer(0, 8) ? 2 : 1;
						if (player.isResting()) {
							ammountTimes += 1;
						}
						boolean berserker = player.getPrayer().usingPrayer(1, 5);
						for (int skill = 0; skill < 25; skill++) {
							if (skill == SkillConstants.SUMMONING) {
								continue;
							}
							for (int time = 0; time < ammountTimes; time++) {
								int currentLevel = player.getSkills().getLevel(skill);
								int normalLevel = player.getSkills().getLevelForXp(skill);
								if (currentLevel > normalLevel) {
									if (skill == SkillConstants.ATTACK || skill == SkillConstants.STRENGTH || skill == SkillConstants.DEFENCE || skill == SkillConstants.RANGE || skill == SkillConstants.MAGIC) {
										if (berserker && Misc.getRandom(100) <= 15) {
											continue;
										}
									}
									player.getSkills().set(skill, currentLevel - 1);
								} else if (currentLevel < normalLevel) {
									player.getSkills().set(skill, currentLevel + 1);
								} else {
									break;
								}
							}
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}, 0, 30000);
		
	}
	
	private static void addRestoreSpecialAttackTask() {
		fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : World.getPlayers()) {
						if (player == null || player.isDead() || !player.isRunning()) {
							continue;
						}
						player.getCombatDefinitions().restoreSpecialAttack();
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}, 0, 30000);
	}
	
	private static void addSummoningEffectTask() {
		slowExecutor.scheduleWithFixedDelay(() -> {
			try {
				for (Player player : World.getPlayers()) {
					if (player.getFamiliar() == null || player.isDead() || !player.hasFinished()) {
						continue;
					}
					if (player.getFamiliar().getOriginalId() == 6814) {
						player.heal(20);
						player.setNextGraphics(new Graphics(1507));
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}, 0, 15, TimeUnit.SECONDS);
	}
	
	private static void addOwnedObjectsTask() {
		slowExecutor.scheduleWithFixedDelay(() -> {
			try {
				OwnedObjectManager.processAll();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	private static void saveFiles() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished()) {
				continue;
			}
			SerializableFilesManager.savePlayer(player);
		}
	}
	
	private static void cleanMemory(boolean force) {
		if (force) {
			ItemDefinitions.clearItemsDefinitions();
			NPCDefinitions.clearNPCDefinitions();
			ObjectDefinitions.clearObjectDefinitions();
			for (Region region : RegionManager.getRegions().values()) {
				region.removeMapFromMemory();
			}
		}
		for (Index index : Cache.STORE.getIndexes()) {
			index.resetCachedFiles();
		}
		fastExecutor.purge();
		System.gc();
	}
	
	public static void safeShutdown(int delay) {
		if (shutdownStart != 0) {
			return;
		}
		shutdownStart = Misc.currentTimeMillis();
		shutdownDelay = delay;
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished()) {
				continue;
			}
			player.getPackets().sendSystemUpdate(delay);
		}
		slowExecutor.schedule(() -> {
			try {
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted()) {
						continue;
					}
					player.realFinish();
				}
				closeServices();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}, delay, TimeUnit.SECONDS);
	}
	
	private static void closeServices() {
		ServerChannelHandler.shutdown();
		shutdown();
	}
	
	private static void shutdown() {
		serverWorkerChannelExecutor.shutdown();
		serverBossChannelExecutor.shutdown();
		fastExecutor.cancel();
		slowExecutor.shutdown();
		shutdown = true;
	}
}
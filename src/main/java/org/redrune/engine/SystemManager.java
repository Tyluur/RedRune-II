package org.redrune.engine;

import com.alex.store.Index;
import org.redrune.cache.Cache;
import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.cache.loaders.NPCDefinitions;
import org.redrune.cache.loaders.ObjectDefinitions;
import org.redrune.engine.cycle.GameCycleWorker;
import org.redrune.engine.factory.DecoderThreadFactory;
import org.redrune.engine.factory.SlowThreadFactory;
import org.redrune.engine.tick.schedule.Scheduler;
import org.redrune.engine.tick.schedule.impl.InformationTabTick;
import org.redrune.engine.tick.schedule.impl.PunishmentProcessorTick;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.link.OwnedObjectManager;
import org.redrune.game.global.World;
import org.redrune.game.global.map.region.Region;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.constants.GameConstants;
import org.redrune.utility.constants.SkillConstants;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.entity.actor.player.PlayerSaving;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Manages all system operations.
 *
 * @author Tyluur <itstyluur@icloud.com>
 * @since 5/21/2017
 */
public final class SystemManager {
	
	/**
	 * Gets the amount of processors on the computer
	 */
	public static final int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
	
	public static final ScheduledExecutorService SLOW_EXECUTOR = PROCESSOR_COUNT >= 6 ? Executors.newScheduledThreadPool(PROCESSOR_COUNT >= 12 ? 4 : 2, new SlowThreadFactory()) : Executors.newSingleThreadScheduledExecutor(new SlowThreadFactory());
	
	private static final Timer FAST_EXECUTOR = new Timer("Fast Executor");
	
	public static final ExecutorService SERVER_WORKER_CHANNEL_EXECUTOR = PROCESSOR_COUNT >= 6 ? Executors.newFixedThreadPool(PROCESSOR_COUNT - (PROCESSOR_COUNT >= 12 ? 7 : 5), new DecoderThreadFactory()) : Executors.newSingleThreadExecutor(new DecoderThreadFactory());
	
	public static final ExecutorService SERVER_BOSS_CHANNEL_EXECUTOR = Executors.newSingleThreadExecutor(new DecoderThreadFactory());
	
	public static final Scheduler SCHEDULER = new Scheduler();
	
	public static int serverWorkersCount;
	
	public static volatile boolean shutdown;
	
	public static long shutdownStart;
	
	public static int shutdownDelay;
	
	private static boolean checkAgility;
	
	private static final GameCycleWorker CYCLE_WORKER = new GameCycleWorker();
	
	private SystemManager() {
	
	}
	
	public static void initialize() {
		serverWorkersCount = PROCESSOR_COUNT >= 6 ? PROCESSOR_COUNT - (PROCESSOR_COUNT >= 12 ? 7 : 5) : 1;
		registerTasks();
		CYCLE_WORKER.start();
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
		addScheduledTasks();
	}
	
	private static void addAccountsSavingTask() {
		SystemManager.SLOW_EXECUTOR.scheduleWithFixedDelay(() -> {
			try {
				saveFiles();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
		}, 15, 15, TimeUnit.SECONDS);
	}
	
	private static void addCleanMemoryTask() {
		SystemManager.SLOW_EXECUTOR.scheduleWithFixedDelay(() -> {
			try {
				cleanMemory(Runtime.getRuntime().freeMemory() < GameConstants.MIN_FREE_MEM_ALLOWED);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}, 0, 10, TimeUnit.MINUTES);
	}
	
	private static void addRestoreRunEnergyTask() {
		FAST_EXECUTOR.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : World.getPlayers()) {
						if (player == null || player.isDead() || !player.isRunning() || (checkAgility && player.getSkills().getLevel(SkillConstants.AGILITY) < 70)) {
							continue;
						}
						player.getAttributes().restoreRunEnergy();
					}
					checkAgility = !checkAgility;
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);
	}
	
	private static void addRestoreHitPointsTask() {
		FAST_EXECUTOR.schedule(new TimerTask() {
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
						if (npc == null || npc.isDead() || npc.isFinished()) {
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
		FAST_EXECUTOR.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : World.getPlayers()) {
						if (player == null || !player.isRunning()) {
							continue;
						}
						int ammountTimes = player.getPrayer().usingPrayer(0, 8) ? 2 : 1;
						if (player.getAttributes().isResting()) {
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
		FAST_EXECUTOR.schedule(new TimerTask() {
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
		SLOW_EXECUTOR.scheduleWithFixedDelay(() -> {
			try {
				for (Player player : World.getPlayers()) {
					if (player.getFamiliar() == null || player.isDead() || !player.isFinished()) {
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
		SLOW_EXECUTOR.scheduleWithFixedDelay(() -> {
			try {
				OwnedObjectManager.processAll();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	private static void addScheduledTasks() {
		SCHEDULER.schedule(new PunishmentProcessorTick());
		SCHEDULER.schedule(new InformationTabTick());
	}
	
	private static void saveFiles() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.isFinished()) {
				continue;
			}
			PlayerSaving.savePlayer(player);
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
		FAST_EXECUTOR.purge();
		System.gc();
	}
	
	public static void safeShutdown(int delay) {
		if (shutdownStart != 0) {
			return;
		}
		shutdownStart = Misc.currentTimeMillis();
		shutdownDelay = delay;
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.isFinished()) {
				continue;
			}
			player.getPackets().sendSystemUpdate(delay);
		}
		SLOW_EXECUTOR.schedule(() -> {
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
		shutdown();
	}
	
	private static void shutdown() {
		SERVER_WORKER_CHANNEL_EXECUTOR.shutdown();
		SERVER_BOSS_CHANNEL_EXECUTOR.shutdown();
		FAST_EXECUTOR.cancel();
		SLOW_EXECUTOR.shutdown();
		shutdown = true;
	}
}
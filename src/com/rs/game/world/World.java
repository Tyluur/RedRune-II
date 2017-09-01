package com.rs.game.world;

import com.rs.Launcher;
import com.rs.cores.CoresManager;
import com.rs.game.GameConstants;
import com.rs.game.content.controller.impl.activity.Wilderness;
import com.rs.game.content.skills.hunter.Hunter.HunterNPC;
import com.rs.game.content.skills.slayer.SlayerHelp;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.ActorList;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.impl.corp.CorporealBeast;
import com.rs.game.entity.actor.npc.impl.dragons.KingBlackDragon;
import com.rs.game.entity.actor.npc.impl.jad.TzTokJad;
import com.rs.game.entity.actor.npc.impl.kalph.KalphiteQueen;
import com.rs.game.entity.actor.npc.impl.normal.Jadinko;
import com.rs.game.entity.actor.npc.impl.normal.Polypore;
import com.rs.game.entity.actor.npc.impl.normal.Slayer;
import com.rs.game.entity.actor.npc.impl.others.*;
import com.rs.game.entity.actor.npc.impl.slayer.Strykewyrm;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.PlayerSkills;
import com.rs.game.entity.actor.player.link.OwnedObjectManager;
import com.rs.game.entity.item.FloorItem;
import com.rs.game.entity.item.Item;
import com.rs.game.entity.item.ItemConstants;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.world.region.Region;
import com.rs.utility.Misc;
import com.rs.utility.Misc.EntityDirection;
import com.rs.utility.networking.AntiFlood;

import java.util.*;
import java.util.concurrent.TimeUnit;

public final class World {
	
	private static final ActorList<Player> players = new ActorList<>(GameConstants.PLAYERS_LIMIT);
	
	private static final ActorList<NPC> npcs = new ActorList<>(GameConstants.NPCS_LIMIT);
	
	private static final Map<Integer, Region> regions = Collections.synchronizedMap(new HashMap<Integer, Region>());
	
	public static int exiting_delay;
	
	public static long exiting_start;
	
	public static boolean restarting;
	
	private static boolean checkAgility;
	
	private World() {
	
	}
	
	public static void init() {
		restarting = false;
		addRestoreRunEnergyTask();
		addRestoreHitPointsTask();
		addRestoreSkillsTask();
		addRestoreSpecialAttackTask();
		addSummoningEffectTask();
		addOwnedObjectsTask();
	}
	
	private static void addRestoreRunEnergyTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead() || !player.isRunning() || (checkAgility && player.getSkills().getLevel(PlayerSkills.AGILITY) < 70)) {
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
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead() || !player.isRunning()) {
							continue;
						}
						player.restoreHitPoints();
					}
					for (NPC npc : npcs) {
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
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || !player.isRunning()) {
							continue;
						}
						int ammountTimes = player.getPrayer().usingPrayer(0, 8) ? 2 : 1;
						if (player.isResting()) {
							ammountTimes += 1;
						}
						boolean berserker = player.getPrayer().usingPrayer(1, 5);
						for (int skill = 0; skill < 25; skill++) {
							if (skill == PlayerSkills.SUMMONING) {
								continue;
							}
							for (int time = 0; time < ammountTimes; time++) {
								int currentLevel = player.getSkills().getLevel(skill);
								int normalLevel = player.getSkills().getLevelForXp(skill);
								if (currentLevel > normalLevel) {
									if (skill == PlayerSkills.ATTACK || skill == PlayerSkills.STRENGTH || skill == PlayerSkills.DEFENCE || skill == PlayerSkills.RANGE || skill == PlayerSkills.MAGIC) {
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
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
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
	
	// removeGround
	private static void addSummoningEffectTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(() -> {
			try {
				for (Player player : getPlayers()) {
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
		CoresManager.slowExecutor.scheduleWithFixedDelay(() -> {
			try {
				OwnedObjectManager.processAll();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	public static ActorList<Player> getPlayers() {
		return players;
	}
	
	@SuppressWarnings("unused")
	
	public static void sendWorldMessage(String message, boolean forStaff) {
		for (Player p : World.getPlayers()) {
			if (p == null || !p.isRunning() || (forStaff && p.getRights() == 0)) {
				continue;
			}
			p.getPackets().sendGameMessage(message);
		}
	}
	
	public static Map<Integer, Region> getRegions() {
		return regions;
	}
	
	public static void addPlayer(Player player) {
		players.add(player);
		AntiFlood.add(player.getSession().getIp());
	}
	
	public static void removePlayer(Player player) {
		players.remove(player);
		AntiFlood.remove(player.getSession().getIp());
	}
	
	public static void addNPC(NPC npc) {
		npcs.add(npc);
	}
	
	public static void removeNPC(NPC npc) {
		npcs.remove(npc);
	}
	
	public static void spawnNPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		spawnNPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}
	
	public static NPC spawnNPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		NPC n;
		HunterNPC hunterNPCs = HunterNPC.forId(id);
		if (hunterNPCs != null) {
			if (id == hunterNPCs.getNpcId()) {
				n = new HuntNPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
			}
		}
		if (SlayerHelp.isSlayer(id)) {
			n = new Slayer(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 6142 || id == 6144 || id == 6145 || id == 6143) {
			n = new PestMonsters(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14688 || id == 14690 || id == 14692 || id == 14694 || id == 14696 || id == 14698 || id == 14700) {
			n = new Polypore(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 13820 || id == 13821 || id == 13822) {
			n = new Jadinko(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 1158 || id == 1160) {
			n = new KalphiteQueen(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 2745) {
			n = new TzTokJad(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 8528) {
			n = new Nomad(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 50 || id == 2642) {
			n = new KingBlackDragon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id >= 9462 && id <= 9467) {
			n = new Strykewyrm(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 8133) {
			n = new CorporealBeast(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14256) {
			n = new Lucien(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 8349 || id == 8450 || id == 8451) {
			n = new TormentedDemon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14301) {
			n = new Glacor(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else {
			n = new NPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		}
		return n;
	}
	
	public static void updateEntityRegion(Actor actor) {
		if (actor.hasFinished()) {
			if (actor instanceof Player) {
				getRegion(actor.getLastRegionId()).removePlayerIndex(actor.getIndex());
			} else {
				getRegion(actor.getLastRegionId()).removeNPCIndex(actor.getIndex());
			}
			return;
		}
		int regionId = actor.getRegionId();
		if (actor.getLastRegionId() != regionId) { // map region actor at
			// changed
			if (actor instanceof Player) {
				if (actor.getLastRegionId() > 0) {
					getRegion(actor.getLastRegionId()).removePlayerIndex(actor.getIndex());
				}
				Region region = getRegion(regionId);
				region.addPlayerIndex(actor.getIndex());
				Player player = (Player) actor;
				player.getControllerManager().moved();
				int musicId = region.getMusicId();
				if (musicId != -1) {
					player.getMusicsManager().checkMusic(region.getMusicId());
				}
				if (player.isRunning() && player.getControllerManager().getController() == null) {
					checkControllersAtMove(player);
				}
			} else {
				if (actor.getLastRegionId() > 0) {
					getRegion(actor.getLastRegionId()).removeNPCIndex(actor.getIndex());
				}
				getRegion(regionId).addNPCIndex(actor.getIndex());
			}
			actor.checkMultiArea();
			actor.setLastRegionId(regionId);
		} else {
			if (actor instanceof Player) {
				Player player = (Player) actor;
				player.getControllerManager().moved();
				if (player.isRunning() && player.getControllerManager().getController() == null) {
					checkControllersAtMove(player);
				}
			}
			actor.checkMultiArea();
		}
	}
	
	public static Region getRegion(int id) {
		return getRegion(id, false);
	}
	
	private static void checkControllersAtMove(Player player) {
		if (Wilderness.isAtWild(player)) {
			player.getControllerManager().startController("Wilderness");
		}
	}
	
	public static Region getRegion(int id, boolean load) {
		Region region = regions.get(id);
		if (region == null) {
			region = new Region(id);
			regions.put(id, region);
		}
		if (load) {
			region.checkLoadMap();
		}
		return region;
	}
	
	/*
	 * checks clip
	 */
	public static boolean canMoveNPC(int plane, int x, int y, int size) {
		for (int tileX = x; tileX < x + size; tileX++) {
			for (int tileY = y; tileY < y + size; tileY++) {
				if (getMask(plane, tileX, tileY) != 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static int getMask(int plane, int x, int y) {
		WorldTile tile = new WorldTile(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null) {
			return -1;
		}
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		return region.getMask(tile.getPlane(), baseLocalX, baseLocalY);
	}
	
	public static void setMask(int plane, int x, int y, int mask) {
		WorldTile tile = new WorldTile(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null) {
			return;
		}
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		region.setMask(tile.getPlane(), baseLocalX, baseLocalY, mask);
	}
	
	public static int getRotation(int plane, int x, int y) {
		WorldTile tile = new WorldTile(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null) {
			return 0;
		}
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		return region.getRotation(tile.getPlane(), baseLocalX, baseLocalY);
	}
	
	public static final NPC spawnNPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, EntityDirection faceDirection) {
		NPC returnValue = spawnNPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
		returnValue.setDirection(faceDirection.getValue());
		return returnValue;
	}
	
	public static boolean checkProjectileStep(int plane, int x, int y, int dir, int size) {
		int xOffset = Misc.DIRECTION_DELTA_X[dir];
		int yOffset = Misc.DIRECTION_DELTA_Y[dir];
		if (size == 1) {
			int mask = getClipedOnlyMask(plane, x + Misc.DIRECTION_DELTA_X[dir], y + Misc.DIRECTION_DELTA_Y[dir]);
			if (xOffset == -1 && yOffset == 0) {
				return (mask & 0x42240000) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (mask & 0x60240000) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (mask & 0x40a40000) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (mask & 0x48240000) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (mask & 0x43a40000) == 0 && (getClipedOnlyMask(plane, x - 1, y) & 0x42240000) == 0 && (getClipedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (mask & 0x60e40000) == 0 && (getClipedOnlyMask(plane, x + 1, y) & 0x60240000) == 0 && (getClipedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (mask & 0x4e240000) == 0 && (getClipedOnlyMask(plane, x - 1, y) & 0x42240000) == 0 && (getClipedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (mask & 0x78240000) == 0 && (getClipedOnlyMask(plane, x + 1, y) & 0x60240000) == 0 && (getClipedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
			}
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0) {
				return (getClipedOnlyMask(plane, x - 1, y) & 0x43a40000) == 0 && (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (getClipedOnlyMask(plane, x + 2, y) & 0x60e40000) == 0 && (getClipedOnlyMask(plane, x + 2, y + 1) & 0x78240000) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (getClipedOnlyMask(plane, x, y - 1) & 0x43a40000) == 0 && (getClipedOnlyMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (getClipedOnlyMask(plane, x, y + 2) & 0x4e240000) == 0 && (getClipedOnlyMask(plane, x + 1, y + 2) & 0x78240000) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (getClipedOnlyMask(plane, x - 1, y) & 0x4fa40000) == 0 && (getClipedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) == 0 && (getClipedOnlyMask(plane, x, y - 1) & 0x63e40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (getClipedOnlyMask(plane, x + 1, y - 1) & 0x63e40000) == 0 && (getClipedOnlyMask(plane, x + 2, y - 1) & 0x60e40000) == 0 && (getClipedOnlyMask(plane, x + 2, y) & 0x78e40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4fa40000) == 0 && (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0 && (getClipedOnlyMask(plane, x, y + 2) & 0x7e240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (getClipedOnlyMask(plane, x + 1, y + 2) & 0x7e240000) == 0 && (getClipedOnlyMask(plane, x + 2, y + 2) & 0x78240000) == 0 && (getClipedOnlyMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
			}
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getClipedOnlyMask(plane, x - 1, y) & 0x43a40000) != 0 || (getClipedOnlyMask(plane, x - 1, -1 + (y + size)) & 0x4e240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getClipedOnlyMask(plane, x + size, y) & 0x60e40000) != 0 || (getClipedOnlyMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x, y - 1) & 0x43a40000) != 0 || (getClipedOnlyMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x, y + size) & 0x4e240000) != 0 || (getClipedOnlyMask(plane, x + (size - 1), y + size) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0) {
						return false;
					}
				}
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x - 1, y + (-1 + sizeOffset)) & 0x4fa40000) != 0 || (getClipedOnlyMask(plane, sizeOffset - 1 + x, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x + size, y - 1) & 0x60e40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x + size, sizeOffset + (-1 + y)) & 0x78e40000) != 0 || (getClipedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x - 1, y + size) & 0x4e240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0 || (getClipedOnlyMask(plane, -1 + (x + sizeOffset), y + size) & 0x7e240000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x + size, y + size) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0 || (getClipedOnlyMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private static int getClipedOnlyMask(int plane, int x, int y) {
		WorldTile tile = new WorldTile(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null) {
			return -1;
		}
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		return region.getMaskClipedOnly(tile.getPlane(), baseLocalX, baseLocalY);
	}
	
	public static final boolean checkWalkStep(int plane, int x, int y, int dir, int size) {
		int xOffset = Misc.DIRECTION_DELTA_X[dir];
		int yOffset = Misc.DIRECTION_DELTA_Y[dir];
		int rotation = getRotation(plane, x + xOffset, y + yOffset);
		if (rotation != 0) {
			for (int rotate = 0; rotate < (4 - rotation); rotate++) {
				int fakeChunckX = xOffset;
				int fakeChunckY = yOffset;
				xOffset = fakeChunckY;
				yOffset = 0 - fakeChunckX;
			}
		}
		
		if (size == 1) {
			int mask = getMask(plane, x + Misc.DIRECTION_DELTA_X[dir], y + Misc.DIRECTION_DELTA_Y[dir]);
			if (xOffset == -1 && yOffset == 0) {
				return (mask & 0x42240000) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (mask & 0x60240000) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (mask & 0x40a40000) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (mask & 0x48240000) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (mask & 0x43a40000) == 0 && (getMask(plane, x - 1, y) & 0x42240000) == 0 && (getMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (mask & 0x60e40000) == 0 && (getMask(plane, x + 1, y) & 0x60240000) == 0 && (getMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (mask & 0x4e240000) == 0 && (getMask(plane, x - 1, y) & 0x42240000) == 0 && (getMask(plane, x, y + 1) & 0x48240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (mask & 0x78240000) == 0 && (getMask(plane, x + 1, y) & 0x60240000) == 0 && (getMask(plane, x, y + 1) & 0x48240000) == 0;
			}
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0) {
				return (getMask(plane, x - 1, y) & 0x43a40000) == 0 && (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (getMask(plane, x + 2, y) & 0x60e40000) == 0 && (getMask(plane, x + 2, y + 1) & 0x78240000) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (getMask(plane, x, y - 1) & 0x43a40000) == 0 && (getMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (getMask(plane, x, y + 2) & 0x4e240000) == 0 && (getMask(plane, x + 1, y + 2) & 0x78240000) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (getMask(plane, x - 1, y) & 0x4fa40000) == 0 && (getMask(plane, x - 1, y - 1) & 0x43a40000) == 0 && (getMask(plane, x, y - 1) & 0x63e40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (getMask(plane, x + 1, y - 1) & 0x63e40000) == 0 && (getMask(plane, x + 2, y - 1) & 0x60e40000) == 0 && (getMask(plane, x + 2, y) & 0x78e40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (getMask(plane, x - 1, y + 1) & 0x4fa40000) == 0 && (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0 && (getMask(plane, x, y + 2) & 0x7e240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (getMask(plane, x + 1, y + 2) & 0x7e240000) == 0 && (getMask(plane, x + 2, y + 2) & 0x78240000) == 0 && (getMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
			}
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getMask(plane, x - 1, y) & 0x43a40000) != 0 || (getMask(plane, x - 1, -1 + (y + size)) & 0x4e240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getMask(plane, x + size, y) & 0x60e40000) != 0 || (getMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getMask(plane, x, y - 1) & 0x43a40000) != 0 || (getMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getMask(plane, x, y + size) & 0x4e240000) != 0 || (getMask(plane, x + (size - 1), y + size) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0) {
						return false;
					}
				}
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getMask(plane, x - 1, y - 1) & 0x43a40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getMask(plane, x - 1, y + (-1 + sizeOffset)) & 0x4fa40000) != 0 || (getMask(plane, sizeOffset - 1 + x, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getMask(plane, x + size, y - 1) & 0x60e40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getMask(plane, x + size, sizeOffset + (-1 + y)) & 0x78e40000) != 0 || (getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getMask(plane, x - 1, y + size) & 0x4e240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0 || (getMask(plane, -1 + (x + sizeOffset), y + size) & 0x7e240000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getMask(plane, x + size, y + size) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0 || (getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public static final boolean containsPlayer(String username) {
		for (Player p2 : players) {
			if (p2 == null) {
				continue;
			}
			if (p2.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	public static Player getPlayer(String username) {
		for (Player player : getPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getUsername().equals(username)) {
				return player;
			}
		}
		return null;
	}
	
	public static final Player getPlayerByDisplayName(String username) {
		String formatedUsername = Misc.formatPlayerNameForProtocol(username);
		for (Player player : getPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getUsername().equals(formatedUsername) || player.getDisplayName().equals(username)) {
				return player;
			}
		}
		return null;
	}
	
	public static ActorList<NPC> getNPCs() {
		return npcs;
	}
	
	public static void safeShutdown(int delay) {
		if (exiting_start != 0) {
			return;
		}
		exiting_start = Misc.currentTimeMillis();
		exiting_delay = delay;
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished()) {
				continue;
			}
			player.getPackets().sendSystemUpdate(delay);
		}
		CoresManager.slowExecutor.schedule(() -> {
			try {
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted()) {
						continue;
					}
					player.realFinish();
				}
				Launcher.restart();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}, delay, TimeUnit.SECONDS);
	}
	
	/*
	 * by default doesnt changeClipData
	 */
	public static void spawnTemporaryObject(final WorldObject object, long time) {
		spawnTemporaryObject(object, time, false);
	}
	
	public static void spawnTemporaryObject(final WorldObject object, long time, final boolean clip) {
		final int regionId = object.getRegionId();
		WorldObject realMapObject = getRegion(regionId).getRealObject(object);
		// remakes object, has to be done because on static region coords arent
		// same of real
		final WorldObject realObject = realMapObject == null ? null : new WorldObject(realMapObject.getId(), realMapObject.getType(), realMapObject.getRotation(), object.getX(), object.getY(), object.getPlane());
		spawnObject(object, clip);
		final int baseLocalX = object.getX() - ((regionId >> 8) * 64);
		final int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
		if (realObject != null && clip) {
			getRegion(regionId).removeMapObject(realObject, baseLocalX, baseLocalY);
		}
		CoresManager.slowExecutor.schedule(() -> {
			try {
				getRegion(regionId).removeObject(object);
				if (clip) {
					getRegion(regionId).removeMapObject(object, baseLocalX, baseLocalY);
					if (realObject != null) {
						int baseLocalX1 = object.getX() - ((regionId >> 8) * 64);
						int baseLocalY1 = object.getY() - ((regionId & 0xff) * 64);
						getRegion(regionId).addMapObject(realObject, baseLocalX1, baseLocalY1);
					}
				}
				for (Player p2 : players) {
					if (p2 == null || !p2.hasStarted() || p2.hasFinished() || !p2.getMapRegionsIds().contains(regionId)) {
						continue;
					}
					if (realObject != null) {
						p2.getPackets().sendSpawnedObject(realObject);
					} else {
						p2.getPackets().sendDestroyObject(object);
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}, time, TimeUnit.MILLISECONDS);
	}
	
	public static void spawnObject(WorldObject object, boolean clip) {
		int regionId = object.getRegionId();
		getRegion(regionId).addObject(object);
		if (clip) {
			int baseLocalX = object.getX() - ((regionId >> 8) * 64);
			int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
			getRegion(regionId).addMapObject(object, baseLocalX, baseLocalY);
		}
		synchronized (players) {
			for (Player p2 : players) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished() || !p2.getMapRegionsIds().contains(regionId)) {
					continue;
				}
				p2.getPackets().sendSpawnedObject(object);
			}
		}
	}
	
	public static boolean isSpawnedObject(WorldObject object) {
		final int regionId = object.getRegionId();
		WorldObject spawnedObject = getRegion(regionId).getSpawnedObject(object);
		return spawnedObject != null && object.getId() == spawnedObject.getId();
	}
	
	public static boolean removeTemporaryObject(final WorldObject object, long time, final boolean clip) {
		final int regionId = object.getRegionId();
		// remakes object, has to be done because on static region coords arent
		// same of real
		final WorldObject realObject = new WorldObject(object.getId(), object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane());
		removeObject(object, clip);
		CoresManager.slowExecutor.schedule(() -> {
			try {
				getRegion(regionId).removeRemovedObject(object);
				if (clip) {
					int baseLocalX = object.getX() - ((regionId >> 8) * 64);
					int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
					getRegion(regionId).addMapObject(realObject, baseLocalX, baseLocalY);
				}
				for (Player p2 : players) {
					if (p2 == null || !p2.hasStarted() || p2.hasFinished() || !p2.getMapRegionsIds().contains(regionId)) {
						continue;
					}
					p2.getPackets().sendSpawnedObject(realObject);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}, time, TimeUnit.MILLISECONDS);
		
		return true;
	}
	
	public static void removeObject(WorldObject object, boolean clip) {
		int regionId = object.getRegionId();
		getRegion(regionId).addRemovedObject(object);
		if (clip) {
			int baseLocalX = object.getX() - ((regionId >> 8) * 64);
			int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
			getRegion(regionId).removeMapObject(object, baseLocalX, baseLocalY);
		}
		synchronized (players) {
			for (Player p2 : players) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished() || !p2.getMapRegionsIds().contains(regionId)) {
					continue;
				}
				p2.getPackets().sendDestroyObject(object);
			}
		}
	}
	
	public static WorldObject getObject(WorldTile tile) {
		int regionId = tile.getRegionId();
		int baseLocalX = tile.getX() - ((regionId >> 8) * 64);
		int baseLocalY = tile.getY() - ((regionId & 0xff) * 64);
		return getRegion(regionId).getObject(tile.getPlane(), baseLocalX, baseLocalY);
	}
	
	public static WorldObject getObject(WorldTile tile, int type) {
		int regionId = tile.getRegionId();
		int baseLocalX = tile.getX() - ((regionId >> 8) * 64);
		int baseLocalY = tile.getY() - ((regionId & 0xff) * 64);
		return getRegion(regionId).getObject(tile.getPlane(), baseLocalX, baseLocalY, type);
	}
	
	public static void addGroundItem(final Item item, final WorldTile tile) {
		final FloorItem floorItem = new FloorItem(item, tile, null, false, false);
		final Region region = getRegion(tile.getRegionId());
		region.forceGetFloorItems().add(floorItem);
		int regionId = tile.getRegionId();
		for (Player player : players) {
			if (player == null || !player.hasStarted() || player.hasFinished() || player.getPlane() != tile.getPlane() || !player.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			addGroundItem(item, tile, player, false, 3600, true);
		}
	}
	
	public static void addGroundItem(final Item item, final WorldTile tile, final Player owner/* null for default */, final boolean underGrave, long hiddenTime/* default 3minutes */, boolean invisible) {
		final FloorItem floorItem = new FloorItem(item, tile, owner, owner != null && underGrave, invisible);
		final Region region = getRegion(tile.getRegionId());
		region.forceGetFloorItems().add(floorItem);
		
		if (invisible && hiddenTime != -1) {
			if (owner != null) {
				owner.getPackets().sendGroundItem(floorItem);
			}
			CoresManager.slowExecutor.schedule(() -> {
				try {
					if (!region.forceGetFloorItems().contains(floorItem)) {
						return;
					}
					int regionId = tile.getRegionId();
					if ((owner != null && underGrave) || ItemConstants.isTradeable(floorItem)) {
						region.forceGetFloorItems().remove(floorItem);
						if (owner.getMapRegionsIds().contains(regionId) || owner.getPlane() != tile.getPlane()) {
							owner.getPackets().sendRemoveGroundItem(floorItem);
						}
						return;
					}
					floorItem.setInvisible(false);
					for (Player player : players) {
						if (player == null || player == owner || !player.hasStarted() || player.hasFinished() || player.getPlane() != tile.getPlane() || !player.getMapRegionsIds().contains(regionId)) {
							continue;
						}
						player.getPackets().sendGroundItem(floorItem);
					}
					removeGroundItem(floorItem, 180);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}, hiddenTime, TimeUnit.SECONDS);
			return;
		}
		int regionId = tile.getRegionId();
		for (Player player : players) {
			if (player == null || !player.hasStarted() || player.hasFinished() || player.getPlane() != tile.getPlane() || !player.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			player.getPackets().sendGroundItem(floorItem);
		}
		removeGroundItem(floorItem, 180);
	}
	
	private static void removeGroundItem(final FloorItem floorItem, long publicTime) {
		CoresManager.slowExecutor.schedule(() -> {
			try {
				int regionId = floorItem.getTile().getRegionId();
				Region region = getRegion(regionId);
				if (!region.forceGetFloorItems().contains(floorItem)) {
					return;
				}
				region.forceGetFloorItems().remove(floorItem);
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted() || player.hasFinished() || player.getPlane() != floorItem.getTile().getPlane() || !player.getMapRegionsIds().contains(regionId)) {
						continue;
					}
					player.getPackets().sendRemoveGroundItem(floorItem);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}, publicTime, TimeUnit.SECONDS);
	}
	
	public static void updateGroundItem(Item item, final WorldTile tile, final Player owner) {
		final FloorItem floorItem = World.getRegion(tile.getRegionId()).getGroundItem(item.getId(), tile, owner);
		if (floorItem == null) {
			addGroundItem(item, tile, owner, false, 360, true);
			return;
		}
		floorItem.setAmount(floorItem.getAmount() + item.getAmount());
		owner.getPackets().sendRemoveGroundItem(floorItem);
		owner.getPackets().sendGroundItem(floorItem);
		
	}
	
	public static boolean removeGroundItem(Player player, FloorItem floorItem) {
		return removeGroundItem(player, floorItem, true);
	}
	
	public static boolean removeGroundItem(Player player, FloorItem floorItem, boolean add) {
		int regionId = floorItem.getTile().getRegionId();
		Region region = getRegion(regionId);
		if (!region.forceGetFloorItems().contains(floorItem)) {
			return false;
		}
		if (player.getInventory().getFreeSlots() == 0) {
			return false;
		}
		region.forceGetFloorItems().remove(floorItem);
		if (add) {
			player.getInventory().addItem(floorItem.getId(), floorItem.getAmount());
		}
		if (floorItem.isInvisible() || floorItem.isGrave()) {
			player.getPackets().sendRemoveGroundItem(floorItem);
			return true;
		} else {
			for (Player p2 : World.getPlayers()) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished() || p2.getPlane() != floorItem.getTile().getPlane() || !p2.getMapRegionsIds().contains(regionId)) {
					continue;
				}
				p2.getPackets().sendRemoveGroundItem(floorItem);
			}
			return true;
		}
	}
	
	public static void sendGraphics(Actor creator, Graphics graphics, WorldTile tile) {
		if (creator == null) {
			for (Player player : World.getPlayers()) {
				if (player == null || !player.hasStarted() || player.hasFinished() || !player.withinDistance(tile)) {
					continue;
				}
				player.getPackets().sendGraphics(graphics, tile);
			}
		} else {
			for (int regionId : creator.getMapRegionsIds()) {
				List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
				if (playersIndexes == null) {
					continue;
				}
				for (Integer playerIndex : playersIndexes) {
					Player player = players.get(playerIndex);
					if (player == null || !player.hasStarted() || player.hasFinished() || !player.withinDistance(tile)) {
						continue;
					}
					player.getPackets().sendGraphics(graphics, tile);
				}
			}
		}
	}
	
	public static void sendProjectile(Actor shooter, WorldTile startTile, WorldTile receiver, int gfxId, int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null || !player.hasStarted() || player.hasFinished() || (!player.withinDistance(shooter) && !player.withinDistance(receiver))) {
					continue;
				}
				player.getPackets().sendProjectile(null, startTile, receiver, gfxId, startHeight, endHeight, speed, delay, curve, startDistanceOffset, 1);
			}
		}
	}
	
	public static void sendProjectile(Actor shooter, WorldTile receiver, int gfxId, int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null || !player.hasStarted() || player.hasFinished() || (!player.withinDistance(shooter) && !player.withinDistance(receiver))) {
					continue;
				}
				player.getPackets().sendProjectile(null, shooter, receiver, gfxId, startHeight, endHeight, speed, delay, curve, startDistanceOffset, shooter.getSize());
			}
		}
	}
	
	public static void sendProjectile(Actor shooter, Actor receiver, int gfxId, int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null || !player.hasStarted() || player.hasFinished() || (!player.withinDistance(shooter) && !player.withinDistance(receiver))) {
					continue;
				}
				int size = shooter.getSize();
				player.getPackets().sendProjectile(receiver, new WorldTile(shooter.getCoordFaceX(size), shooter.getCoordFaceY(size), shooter.getPlane()), receiver, gfxId, startHeight, endHeight, speed, delay, curve, startDistanceOffset, size);
			}
		}
	}
	
	public static boolean isMultiArea(WorldTile tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return (destX >= 3200 && destX <= 3390 && destY >= 3840 && destY <= 3967) // wild
				       || (destX >= 2835 && destX <= 2880 && destY >= 5905 && destY <= 5950) || (destX >= 2992 && destX <= 3007 && destY >= 3912 && destY <= 3967) || (destX >= 2946 && destX <= 2959 && destY >= 3816 && destY <= 3831) || (destX >= 3008 && destX <= 3199 && destY >= 3856 && destY <= 3903) || (destX >= 3008 && destX <= 3071 && destY >= 3600 && destY <= 3711) || (destX >= 3270 && destX <= 3346 && destY >= 3532 && destY <= 3625) || (destX >= 2965 && destX <= 3050 && destY >= 3904 && destY <= 3959) // wild
				       || (destX >= 2815 && destX <= 2966 && destY >= 5240 && destY <= 5375) || (destX >= 2840 && destX <= 2950 && destY >= 5190 && destY <= 5230) // godwars
				       || (destX >= 3547 && destX <= 3555 && destY >= 9690 && destY <= 9699) // zaros
				       // godwars
				       || KingBlackDragon.atKBD(tile) // King Black Dragon lair
				       || TormentedDemon.atTD(tile) // Tormented demon's area
				       || (destX >= 2970 && destX <= 3000 && destY >= 4365 && destY <= 4400)// corp
				       || (destX >= 3136 && destX <= 3327 && destY >= 3520 && destY <= 3970 || (destX >= 2376 && 5127 >= destY && destX <= 2422 && 5168 <= destY)) || (destX >= 2374 && destY >= 5129 && destX <= 2424 && destY <= 5168) // pits
				       || (destX >= 2622 && destY >= 5696 && destX <= 2573 && destY <= 5752) // torms
				       || (destX >= 2368 && destY >= 3072 && destX <= 2431 && destY <= 3135) // castlewars
				       || (destX >= 3086 && destY >= 5536 && destX <= 3315 && destY <= 5530) // Bork
				       || (tile.getX() >= 3526 && tile.getX() <= 3550 && tile.getY() >= 5185 && tile.getY() <= 5215) // out
				       || (destX >= 2365 && destY >= 9470 && destX <= 2436 && destY <= 9532); // castlewars
		// in
		
		// multi
	}
	
	public static boolean isPvpArea(WorldTile tile) {
		return Wilderness.isAtWild(tile);
	}
	
	public static void destroySpawnedObject(WorldObject object, boolean clip) {
		int regionId = object.getRegionId();
		int baseLocalX = object.getX() - ((regionId >> 8) * 64);
		int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
		WorldObject realMapObject = getRegion(regionId).getRealObject(object);
		
		World.getRegion(regionId).removeObject(object);
		if (clip) {
			World.getRegion(regionId).removeMapObject(object, baseLocalX, baseLocalY);
		}
		for (Player p2 : World.getPlayers()) {
			if (p2 == null || !p2.hasStarted() || p2.hasFinished() || !p2.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			if (realMapObject != null) {
				p2.getPackets().sendSpawnedObject(realMapObject);
			} else {
				p2.getPackets().sendDestroyObject(object);
			}
		}
	}
	
	public static void spawnTempGroundObject(final WorldObject object, final int replaceId, long time) {
		final int regionId = object.getRegionId();
		WorldObject realMapObject = getRegion(regionId).getRealObject(object);
		final WorldObject realObject = realMapObject == null ? null : new WorldObject(realMapObject.getId(), realMapObject.getType(), realMapObject.getRotation(), object.getX(), object.getY(), object.getPlane());
		spawnObject(object, false);
		CoresManager.slowExecutor.schedule(() -> {
			try {
				getRegion(regionId).removeObject(object);
				addGroundItem(new Item(replaceId), object, null, false, 180, false);
				for (Player p2 : players) {
					if (p2 == null || !p2.hasStarted() || p2.hasFinished() || p2.getPlane() != object.getPlane() || !p2.getMapRegionsIds().contains(regionId)) {
						continue;
					}
					if (realObject != null) {
						p2.getPackets().sendSpawnedObject(realObject);
					} else {
						p2.getPackets().sendDestroyObject(object);
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}, time, TimeUnit.MILLISECONDS);
	}
	
	public static Player getPlayerByIndex(int index) {
		for (Player player : getPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getIndex() == index) {
				return player;
			}
		}
		return null;
	}
	
	public static void sendObjectAnimation(Actor creator, WorldObject object, Animation animation) {
		if (creator == null) {
			for (Player player : World.getPlayers()) {
				if (player == null || !player.hasStarted() || player.hasFinished() || !player.withinDistance(object)) {
					continue;
				}
				player.getPackets().sendObjectAnimation(object, animation);
			}
		} else {
			for (int regionId : creator.getMapRegionsIds()) {
				List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
				if (playersIndexes == null) {
					continue;
				}
				for (Integer playerIndex : playersIndexes) {
					Player player = players.get(playerIndex);
					if (player == null || !player.hasStarted() || player.hasFinished() || !player.withinDistance(object)) {
						continue;
					}
					player.getPackets().sendObjectAnimation(object, animation);
				}
			}
		}
	}
	
}

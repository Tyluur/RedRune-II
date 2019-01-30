package org.redrune.game.entity.actor.player.data;

import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.constants.SkillConstants;
import org.redrune.utility.functions.Misc;

import java.io.Serializable;
import java.util.List;

public class PlayerPrayer implements Serializable {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -2082861520556582824L;
	
	private final static int[][] prayerLvls = {
			// normal prayer book
			{ 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 35, 37, 40, 43, 44, 45, 46, 49, 52, 60, 65, 70, 74, 77 },
			// ancient prayer book
			{ 50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82, 84, 86, 89, 92, 95 } };
	
	private final static int[][][] closePrayers = { { // normal prayer book
			{ 0, 5, 13 }, // Skin prayers 0
			{ 1, 6, 14 }, // Strength prayers 1
			{ 2, 7, 15 }, // Attack prayers 2
			{ 3, 11, 20, 28 }, // Range prayers 3
			{ 4, 12, 21, 29 }, // Magic prayers 4
			{ 8, 9, 26 }, // Restore prayers 5
			{ 10 }, // Protect item prayers 6
			{ 17, 18, 19 }, // Protect prayers 7
			{ 16 }, // Other protect prayers 8
			{ 22, 23, 24 }, // Other special prayers 9
			{ 25, 27 } // Other prayers 10
	}, { // ancient prayer book
			{ 0 }, // Protect item prayers 0
			{ 1, 2, 3, 4 }, // sap prayers 1
			{ 5 }, // other prayers 2
			{ 7, 8, 9, 17, 18 }, // protect prayers 3
			{ 6 }, // other protect prayers 4
			{ 10, 11, 12, 13, 14, 15, 16 }, // leech prayers 5
			{ 19 }, // other prayers
	} };
	
	private final static int[] prayerSlotValues = { 1, 2, 4, 262144, 524288, 8, 16, 32, 64, 128, 256, 1048576, 2097152, 512, 1024, 2048, 16777216, 4096, 8192, 16384, 4194304, 8388608, 32768, 65536, 131072, 33554432, 134217728, 67108864, 268435456 * 2, 268435456 };
	
	public int hitpoints;
	
	private boolean[][] quickPrayers;
	
	private int prayerpoints;
	
	private boolean ancientcurses;
	
	private transient Player player;
	
	private transient boolean[][] onPrayers;
	
	private transient boolean usingQuickPrayer;
	
	private transient int onPrayersCount;
	
	private transient int[] leechBonuses;
	
	private transient int drainDelay;
	
	private transient boolean boostedLeech;
	
	public PlayerPrayer() {
		quickPrayers = new boolean[][] { new boolean[30], new boolean[20] };
		prayerpoints = 10;
	}
	
	public double getMageMultiplier() {
		if (onPrayersCount == 0) {
			return 1.0;
		}
		double value = 1.0;
		
		// normal
		if (usingPrayer(0, 4)) {
			value += 0.05;
		} else if (usingPrayer(0, 12)) {
			value += 0.10;
		} else if (usingPrayer(0, 21)) {
			value += 0.15;
		} else if (usingPrayer(0, 29)) {
			value += 0.20;
		} else if (usingPrayer(1, 3)) {
			double d = (leechBonuses[2]);
			value += d / 100;
		} else if (usingPrayer(1, 12)) {
			double d = (5 + leechBonuses[5]);
			value += d / 100;
		}
		return value;
	}
	
	public boolean usingPrayer(int book, int prayerId) {
		return onPrayers[book][prayerId];
	}
	
	public double getRangeMultiplier() {
		if (onPrayersCount == 0) {
			return 1.0;
		}
		double value = 1.0;
		
		// normal
		if (usingPrayer(0, 3)) {
			value += 0.05;
		} else if (usingPrayer(0, 11)) {
			value += 0.10;
		} else if (usingPrayer(0, 20)) {
			value += 0.15;
		} else if (usingPrayer(0, 28)) {
			value += 0.20;
		} else if (usingPrayer(1, 2)) {
			double d = (leechBonuses[1]);
			value += d / 100;
		} else if (usingPrayer(1, 11)) {
			double d = (5 + leechBonuses[4]);
			value += d / 100;
		}
		return value;
	}
	
	public double getAttackMultiplier() {
		if (onPrayersCount == 0) {
			return 1.0;
		}
		double value = 1.0;
		
		// normal
		if (usingPrayer(0, 2)) {
			value += 0.05;
		} else if (usingPrayer(0, 7)) {
			value += 0.10;
		} else if (usingPrayer(0, 15)) {
			value += 0.15;
		} else if (usingPrayer(0, 25)) {
			value += 0.15;
		} else if (usingPrayer(0, 27)) {
			value += 0.20;
		} else if (usingPrayer(1, 1)) {
			double d = (leechBonuses[0]);
			value += d / 100;
		} else if (usingPrayer(1, 10)) {
			double d = (5 + leechBonuses[3]);
			value += d / 100;
		} else if (usingPrayer(1, 19)) {
			double d = (15 + leechBonuses[8]);
			value += d / 100;
		}
		return value;
	}
	
	public double getStrengthMultiplier() {
		if (onPrayersCount == 0) {
			return 1.0;
		}
		double value = 1.0;
		
		// normal
		if (usingPrayer(0, 1)) {
			value += 0.05;
		} else if (usingPrayer(0, 6)) {
			value += 0.10;
		} else if (usingPrayer(0, 14)) {
			value += 0.15;
		} else if (usingPrayer(0, 25)) {
			value += 0.18;
		} else if (usingPrayer(0, 27)) {
			value += 0.23;
		} else if (usingPrayer(1, 1)) {
			double d = (leechBonuses[0]);
			value += d / 100;
		} else if (usingPrayer(1, 14)) {
			double d = (5 + leechBonuses[7]);
			value += d / 100;
		} else if (usingPrayer(1, 19)) {
			double d = (23 + leechBonuses[10]);
			value += d / 100;
		}
		return value;
	}
	
	public double getDefenceMultiplier() {
		if (onPrayersCount == 0) {
			return 1.0;
		}
		double value = 1.0;
		
		// normal
		if (usingPrayer(0, 0)) {
			value += 0.05;
		} else if (usingPrayer(0, 5)) {
			value += 0.10;
		} else if (usingPrayer(0, 13)) {
			value += 0.15;
		} else if (usingPrayer(0, 25)) {
			value += 0.20;
		} else if (usingPrayer(0, 27)) {
			value += 0.25;
		} else if (usingPrayer(0, 28)) {
			value += 0.25;
		} else if (usingPrayer(0, 29)) {
			value += 0.25;
		} else if (usingPrayer(1, 1)) {
			double d = (leechBonuses[0]);
			value += d / 100;
		} else if (usingPrayer(1, 13)) {
			double d = (6 + leechBonuses[6]);
			value += d / 100;
		} else if (usingPrayer(1, 19)) {
			double d = (15 + leechBonuses[9]);
			value += d / 100;
		}
		return value;
	}
	
	public int getPrayerHeadIcon() {
		if (onPrayersCount == 0) {
			return -1;
		}
		int value = -1;
		if (usingPrayer(0, 16)) {
			value += 8;
		}
		if (usingPrayer(0, 17)) {
			value += 3;
		} else if (usingPrayer(0, 18)) {
			value += 2;
		} else if (usingPrayer(0, 19)) {
			value += 1;
		} else if (usingPrayer(0, 22)) {
			value += 4;
		} else if (usingPrayer(0, 23)) {
			value += 6;
		} else if (usingPrayer(0, 24)) {
			value += 5;
		} else if (usingPrayer(1, 6)) {
			value += 16;
			if (usingPrayer(1, 8)) {
				value += 2;
			} else if (usingPrayer(1, 7)) {
				value += 3;
			} else if (usingPrayer(1, 9)) {
				value += 1;
			}
		} else if (usingPrayer(1, 7)) {
			value += 14;
		} else if (usingPrayer(1, 8)) {
			value += 15;
		} else if (usingPrayer(1, 9)) {
			value += 13;
		} else if (usingPrayer(1, 17)) {
			value += 20;
		} else if (usingPrayer(1, 18)) {
			value += 21;
		}
		return value;
	}
	
	public void switchSettingQuickPrayer() {
		usingQuickPrayer = !usingQuickPrayer;
		player.getPackets().sendGlobalConfig(181, usingQuickPrayer ? 1 : 0);// activates
		// quick
		// choose
		unlockPrayerBookButtons();
		if (usingQuickPrayer) // switchs tab to prayer
		{
			player.getPackets().sendGlobalConfig(168, 6);
		}
	}
	
	public void unlockPrayerBookButtons() {
		player.getPackets().sendUnlockIComponentOptionSlots(271, usingQuickPrayer ? 42 : 8, 0, 29, 0);
	}
	
	public void switchQuickPrayers() {
		if (!checkPrayer()) {
			return;
		}
		if (hasPrayersOn()) {
			closeAllPrayers();
		} else {
			boolean hasOn = false;
			int index = 0;
			for (boolean prayer : quickPrayers[getPrayerBook()]) {
				if (prayer) {
					if (usePrayer(index)) {
						hasOn = true;
					}
				}
				index++;
			}
			if (hasOn) {
				player.getPackets().sendGlobalConfig(182, 1);
				recalculatePrayer();
			}
		}
	}
	
	private boolean checkPrayer() {
		if (prayerpoints <= 0) {
			player.getPackets().sendSound(2672, 0, 1);
			player.getPackets().sendGameMessage("Please recharge your prayer at the Lumbridge Church.");
			return false;
		}
		return true;
	}
	
	public boolean hasPrayersOn() {
		return onPrayersCount > 0;
	}
	
	public void closeAllPrayers() {
		onPrayers = new boolean[][] { new boolean[30], new boolean[20] };
		leechBonuses = new int[11];
		onPrayersCount = 0;
		player.getPackets().sendGlobalConfig(182, 0);
		player.getPackets().sendConfig(ancientcurses ? 1582 : 1395, 0);
		player.getAppearance().generateAppearanceData();
		resetStatAdjustments();
	}
	
	private int getPrayerBook() {
		return ancientcurses == false ? 0 : 1;
	}
	
	private boolean usePrayer(int prayerId) {
		if (prayerId < 0 || prayerId >= prayerLvls[getPrayerBook()].length) {
			return false;
		}
		if (player.getSkills().getLevelForXp(5) < prayerLvls[this.getPrayerBook()][prayerId]) {
			player.getPackets().sendGameMessage("You need a prayer level of at least " + prayerLvls[getPrayerBook()][prayerId] + " to use this prayer.");
			return false;
		}
		if (getPrayerBook() == 0 && prayerId == 25 || prayerId == 27) {
			if (player.getSkills().getLevelForXp(SkillConstants.DEFENCE) < 60) {
				player.getPackets().sendGameMessage("You need a defence level of at least " + 60 + " to use this prayer.");
				return false;
			}
		} else if (getPrayerBook() == 1) {
			if (player.getSkills().getLevelForXp(SkillConstants.DEFENCE) < 30) {
				player.getPackets().sendGameMessage("You need a defence level of at least " + 30 + " to use this prayer.");
				return false;
			}
		}
		if (player.getAttributes().getPrayerDelay() >= Misc.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You are currently injured and cannot use protection prayers!");
			if (ancientcurses && prayerId >= 6 && prayerId <= 9) {
				return false;
			} else if (prayerId >= 16 && prayerId <= 19) {
				return false;
			}
		}
		if (!usingQuickPrayer) {
			if (onPrayers[getPrayerBook()][prayerId]) {
				onPrayers[getPrayerBook()][prayerId] = false;
				closePrayers(prayerId);
				onPrayersCount--;
				player.getAppearance().generateAppearanceData();
				player.getPackets().sendSound(2663, 0, 1);
				return true;
			}
		} else {
			if (quickPrayers[getPrayerBook()][prayerId]) {
				quickPrayers[getPrayerBook()][prayerId] = false;
				player.getPackets().sendSound(2663, 0, 1);
				return true;
			}
		}
		boolean needAppearenceGenerate = false;
		if (getPrayerBook() == 0) {
			switch (prayerId) {
				case 0:
				case 5:
				case 13:
					closePrayers(closePrayers[getPrayerBook()][0], closePrayers[getPrayerBook()][10]);
					break;
				case 1:
				case 6:
				case 14:
					closePrayers(closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][3], closePrayers[getPrayerBook()][4], closePrayers[getPrayerBook()][10]);
					break;
				case 2:
				case 7:
				case 15:
					closePrayers(closePrayers[getPrayerBook()][2], closePrayers[getPrayerBook()][3], closePrayers[getPrayerBook()][4], closePrayers[getPrayerBook()][10]);
					break;
				case 3:
				case 11:
				case 20:
					closePrayers(closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][2], closePrayers[getPrayerBook()][3], closePrayers[getPrayerBook()][10]);
					break;
				case 4:
				case 12:
				case 21:
					closePrayers(closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][2], closePrayers[getPrayerBook()][4], closePrayers[getPrayerBook()][10]);
					break;
				case 8:
				case 9:
				case 26:
					closePrayers(closePrayers[getPrayerBook()][5]);
					break;
				case 10:
					closePrayers(closePrayers[getPrayerBook()][6]);
					break;
				case 17:
				case 18:
				case 19:
					closePrayers(closePrayers[getPrayerBook()][7], closePrayers[getPrayerBook()][9]);
					needAppearenceGenerate = true;
					break;
				case 16:
					closePrayers(closePrayers[getPrayerBook()][8], closePrayers[getPrayerBook()][9]);
					needAppearenceGenerate = true;
					break;
				case 22:
				case 23:
				case 24:
					closePrayers(closePrayers[getPrayerBook()][7], closePrayers[getPrayerBook()][8], closePrayers[getPrayerBook()][9]);
					needAppearenceGenerate = true;
					break;
				case 25:
				case 27:
				case 28:
				case 29:
					closePrayers(closePrayers[getPrayerBook()][0], closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][2], closePrayers[getPrayerBook()][3], closePrayers[getPrayerBook()][4], closePrayers[getPrayerBook()][10]);
					break;
				default:
					return false;
			}
		} else {
			switch (prayerId) {
				case 0:
					if (!usingQuickPrayer) {
						player.setNextAnimationNoPriority(new Animation(12567));
						player.setNextGraphics(new Graphics(2213));
					}
					closePrayers(closePrayers[getPrayerBook()][0]);
					break;
				case 1:
				case 2:
				case 3:
				case 4:
					closePrayers(closePrayers[getPrayerBook()][5], closePrayers[getPrayerBook()][6]);
					break;
				case 5:
					if (!usingQuickPrayer) {
						player.setNextAnimationNoPriority(new Animation(12589));
						player.setNextGraphics(new Graphics(2266));
					}
					closePrayers(closePrayers[getPrayerBook()][2]);
					break;
				case 7:
				case 8:
				case 9:
				case 17:
				case 18:
					closePrayers(closePrayers[getPrayerBook()][3]);
					needAppearenceGenerate = true;
					break;
				case 6:
					closePrayers(closePrayers[getPrayerBook()][4]);
					needAppearenceGenerate = true;
					break;
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
					closePrayers(closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][6]);
					break;
				case 19:
					if (!usingQuickPrayer) {
						player.setNextAnimationNoPriority(new Animation(12565));
						player.setNextGraphics(new Graphics(2226));
					}
					closePrayers(closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][5], closePrayers[getPrayerBook()][6]);
					break;
				default:
					return false;
			}
		}
		if (!usingQuickPrayer) {
			onPrayers[getPrayerBook()][prayerId] = true;
			if (onPrayersCount == 0) {
				resetDrainPrayer();
			}
			onPrayersCount++;
			if (needAppearenceGenerate) {
				player.getAppearance().generateAppearanceData();
			}
		} else {
			quickPrayers[getPrayerBook()][prayerId] = true;
		}
		player.getPackets().sendSound(2662, 0, 1);
		return true;
	}
	
	public void recalculatePrayer() {
		int value = 0;
		int index = 0;
		for (boolean prayer : (!usingQuickPrayer ? onPrayers[getPrayerBook()] : quickPrayers[getPrayerBook()])) {
			if (prayer) {
				value += ancientcurses ? Math.pow(2, index) : prayerSlotValues[index];
			}
			index++;
		}
		player.getPackets().sendConfig(ancientcurses ? (usingQuickPrayer ? 1587 : 1582) : (usingQuickPrayer ? 1397 : 1395), value);
	}
	
	public void resetStatAdjustments() {
		for (int i = 0; i < 5; i++) {
			adjustStat(i, 0);
		}
	}
	
	public void closePrayers(int prayerId) {
		if (ancientcurses) {
			if (prayerId == 1) {
				if (leechBonuses[0] > 0) {
					player.getPackets().sendGameMessage("Your Attack is now unaffected by sap and leech curses.", true);
				}
				adjustStat(0, 0);
				adjustStat(1, 0);
				adjustStat(2, 0);
				leechBonuses[0] = 0;
			} else if (prayerId == 2) {
				if (leechBonuses[1] > 0) {
					player.getPackets().sendGameMessage("Your Range is now unaffected by sap and leech curses.", true);
				}
				adjustStat(2, 0);
				adjustStat(4, 0);
				leechBonuses[1] = 0;
			} else if (prayerId == 3) {
				if (leechBonuses[2] > 0) {
					player.getPackets().sendGameMessage("Your Magic is now unaffected by sap and leech curses.", true);
				}
				adjustStat(2, 0);
				adjustStat(5, 0);
				leechBonuses[2] = 0;
			} else if (prayerId == 10) {
				if (leechBonuses[3] > 0) {
					player.getPackets().sendGameMessage("Your Attack is now unaffected by sap and leech curses.", true);
				}
				adjustStat(0, 0);
				leechBonuses[3] = 0;
			} else if (prayerId == 11) {
				if (leechBonuses[4] > 0) {
					player.getPackets().sendGameMessage("Your Ranged is now unaffected by sap and leech curses.", true);
				}
				adjustStat(4, 0);
				leechBonuses[4] = 0;
			} else if (prayerId == 12) {
				if (leechBonuses[5] > 0) {
					player.getPackets().sendGameMessage("Your Magic is now unaffected by sap and leech curses.", true);
				}
				adjustStat(5, 0);
				leechBonuses[5] = 0;
			} else if (prayerId == 13) {
				if (leechBonuses[6] > 0) {
					player.getPackets().sendGameMessage("Your Defence is now unaffected by sap and leech curses.", true);
				}
				adjustStat(2, 0);
				leechBonuses[6] = 0;
			} else if (prayerId == 14) {
				if (leechBonuses[7] > 0) {
					player.getPackets().sendGameMessage("Your Strength is now unaffected by sap and leech curses.", true);
				}
				adjustStat(1, 0);
				leechBonuses[7] = 0;
			} else if (prayerId == 19) {
				leechBonuses[8] = 0;
				leechBonuses[9] = 0;
				leechBonuses[10] = 0;
				adjustStat(0, 0);
				adjustStat(1, 0);
				adjustStat(2, 0);
			}
		}
	}
	
	public void closePrayers(int[]... prayers) {
		for (int[] prayer : prayers) {
			for (int prayerId : prayer) {
				if (usingQuickPrayer) {
					quickPrayers[getPrayerBook()][prayerId] = false;
				} else {
					if (onPrayers[getPrayerBook()][prayerId]) {
						onPrayersCount--;
					}
					onPrayers[getPrayerBook()][prayerId] = false;
					closePrayers(prayerId);
				}
			}
		}
	}
	
	public void resetDrainPrayer() {
		drainDelay = 3;
	}
	
	public void adjustStat(int stat, int percentage) {
		player.getPackets().sendConfigByFile(6857 + stat, 30 + percentage);
	}
	
	public void setPrayerBook(boolean ancientcurses) {
		closeAllPrayers();
		this.ancientcurses = ancientcurses;
		player.getInterfaceManager().sendPrayerBook();
		refresh();
	}
	
	public void refresh() {
		player.getPackets().sendGlobalConfig(181, usingQuickPrayer ? 1 : 0);
		player.getPackets().sendConfig(1584, ancientcurses ? 1 : 0);
		unlockPrayerBookButtons();
	}
	
	public void switchPrayer(int prayerId) {
		if (!usingQuickPrayer) {
			if (!checkPrayer()) {
				return;
			}
		}
		usePrayer(prayerId);
		recalculatePrayer();
	}
	
	public void processPrayer() {
		if (!hasPrayersOn()) {
			return;
		}
		boostedLeech = false;
		if (drainDelay <= 0) {
			drainPrayer(5);
			resetDrainPrayer();
		}
		drainDelay--;
		if (!checkPrayer()) {
			closeAllPrayers();
		}
	}
	
	public void drainPrayer(int amount) {
		if ((prayerpoints - amount) >= 0) {
			prayerpoints -= amount;
		} else {
			prayerpoints = 0;
		}
		refreshPrayerPoints();
	}
	
	public void refreshPrayerPoints() {
		player.getPackets().sendConfig(2382, prayerpoints);
	}
	
	public int getOnPrayersCount() {
		return onPrayersCount;
	}
	
	public void init() {
		player.getPackets().sendGlobalConfig(181, usingQuickPrayer ? 1 : 0);
		player.getPackets().sendConfig(1584, ancientcurses ? 1 : 0);
		resetStatAdjustments();
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		onPrayers = new boolean[][] { new boolean[30], new boolean[20] };
		leechBonuses = new int[11];
	}
	
	public boolean isAncientCurses() {
		return ancientcurses;
	}
	
	public boolean isUsingQuickPrayer() {
		return usingQuickPrayer;
	}
	
	public int getPrayerpoints() {
		return prayerpoints;
	}
	
	public void setPrayerpoints(int prayerpoints) {
		this.prayerpoints = prayerpoints;
	}
	
	public void drainPrayerOnHalf() {
		if (prayerpoints > 0) {
			prayerpoints = prayerpoints / 2;
			refreshPrayerPoints();
		}
	}
	
	/*public void restoreHitPoints(int amount) {
		int maxHp = player.getSkills().getLevelForXp(Skills.HITPOINTS);
		if ((prayerpoints + amount) <= maxHp)
			prayerpoints += amount;
		else
			prayerpoints = maxHp;
		refreshHitPoints();
	}*/ //not needed
	public void restorePrayer(int amount) {
		int maxPrayer = player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 10;
		if ((prayerpoints + amount) <= maxPrayer) {
			prayerpoints += amount;
		} else {
			prayerpoints = maxPrayer;
		}
		refreshPrayerPoints();
	}
	
	public void reset() {
		closeAllPrayers();
		prayerpoints = player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 10;
		refreshPrayerPoints();
	}
	
	/**
	 * Sends the redemption prayer effect on death
	 *
	 * @param source
	 * 		The source of the kill
	 */
	public void sendRedemption(Actor source) {
		player.setNextGraphics(new Graphics(437));
		final Player target = player;
		PlayerPrayer prayerInstance = this;
		if (player.isInMultiArea()) {
			for (int regionId : player.getMapRegionsIds()) {
				List<Integer> playersIndexes = RegionManager.getRegion(regionId).getPlayerIndexes();
				if (playersIndexes != null) {
					for (int playerIndex : playersIndexes) {
						Player player = World.getPlayers().get(playerIndex);
						if (player == null || !player.hasStarted() || player.isDead() || player.isFinished() || !player.withinDistance(prayerInstance.player, 2) || !prayerInstance.player.getControllerManager().canHit(player)) {
							continue;
						}
						player.applyHit(new Hit(target, Misc.getRandom((int) (player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 2.5)), HitSplat.REGULAR_DAMAGE));
					}
				}
				List<Integer> npcsIndexes = RegionManager.getRegion(regionId).getNPCsIndexes();
				if (npcsIndexes != null) {
					for (int npcIndex : npcsIndexes) {
						NPC npc = World.getNPCs().get(npcIndex);
						if (npc == null || npc.isDead() || npc.isFinished() || !npc.withinDistance(prayerInstance.player, 2) || !npc.getDefinitions().hasAttackOption() || !prayerInstance.player.getControllerManager().canHit(npc)) {
							continue;
						}
						npc.applyHit(new Hit(target, Misc.getRandom((int) (player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 2.5)), HitSplat.REGULAR_DAMAGE));
					}
				}
			}
		} else {
			if (source != null && source != player && !source.isDead() && !source.isFinished() && source.withinDistance(player, 1)) {
				source.applyHit(new Hit(target, Misc.getRandom((int) (player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 2.5)), HitSplat.REGULAR_DAMAGE));
			}
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() - 1, target.getY(), target.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() + 1, target.getY(), target.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX(), target.getY() - 1, target.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX(), target.getY() + 1, target.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() - 1, target.getY() - 1, target.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() - 1, target.getY() + 1, target.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() + 1, target.getY() - 1, target.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() + 1, target.getY() + 1, target.getPlane()));
			}
		});
	}
	
	/**
	 * Sends the wrath prayer effect on death
	 *
	 * @param source
	 * 		The source of the kill
	 */
	public void sendWrath(Actor source) {
		RegionManager.sendProjectile(player, new WorldTile(player.getX() + 2, player.getY() + 2, player.getPlane()), 2260, 24, 0, 41, 35, 30, 0);
		RegionManager.sendProjectile(player, new WorldTile(player.getX() + 2, player.getY(), player.getPlane()), 2260, 41, 0, 41, 35, 30, 0);
		RegionManager.sendProjectile(player, new WorldTile(player.getX() + 2, player.getY() - 2, player.getPlane()), 2260, 41, 0, 41, 35, 30, 0);
		
		RegionManager.sendProjectile(player, new WorldTile(player.getX() - 2, player.getY() + 2, player.getPlane()), 2260, 41, 0, 41, 35, 30, 0);
		RegionManager.sendProjectile(player, new WorldTile(player.getX() - 2, player.getY(), player.getPlane()), 2260, 41, 0, 41, 35, 30, 0);
		RegionManager.sendProjectile(player, new WorldTile(player.getX() - 2, player.getY() - 2, player.getPlane()), 2260, 41, 0, 41, 35, 30, 0);
		
		RegionManager.sendProjectile(player, new WorldTile(player.getX(), player.getY() + 2, player.getPlane()), 2260, 41, 0, 41, 35, 30, 0);
		RegionManager.sendProjectile(player, new WorldTile(player.getX(), player.getY() - 2, player.getPlane()), 2260, 41, 0, 41, 35, 30, 0);
		final Player target = player;
		PlayerPrayer prayerInstance = this;
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextGraphics(new Graphics(2259));
				if (player.isInMultiArea()) {
					for (int regionId : player.getMapRegionsIds()) {
						List<Integer> playersIndexes = RegionManager.getRegion(regionId).getPlayerIndexes();
						if (playersIndexes != null) {
							for (int playerIndex : playersIndexes) {
								Player player = World.getPlayers().get(playerIndex);
								if (player == null || !player.hasStarted() || player.isDead() || player.isFinished() || !player.withinDistance(prayerInstance.player, 2) || !prayerInstance.player.getControllerManager().canHit(player)) {
									continue;
								}
								player.applyHit(new Hit(target, Misc.getRandom(player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 3), HitSplat.REGULAR_DAMAGE));
							}
						}
						List<Integer> npcsIndexes = RegionManager.getRegion(regionId).getNPCsIndexes();
						if (npcsIndexes != null) {
							for (int npcIndex : npcsIndexes) {
								NPC npc = World.getNPCs().get(npcIndex);
								if (npc == null || npc.isDead() || npc.isFinished() || !npc.withinDistance(prayerInstance.player, 2) || !npc.getDefinitions().hasAttackOption() || !prayerInstance.player.getControllerManager().canHit(npc)) {
									continue;
								}
								npc.applyHit(new Hit(target, Misc.getRandom(player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 3), HitSplat.REGULAR_DAMAGE));
							}
						}
					}
				} else {
					if (source != null && source != target && !source.isDead() && !source.isFinished() && source.withinDistance(target, 2)) {
						source.applyHit(new Hit(target, Misc.getRandom(player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 3), HitSplat.REGULAR_DAMAGE));
					}
				}
				
				RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(player.getX() + 2, player.getY() + 2, player.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(player.getX() + 2, player.getY(), player.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(player.getX() + 2, player.getY() - 2, player.getPlane()));
				
				RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(player.getX() - 2, player.getY() + 2, player.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(player.getX() - 2, player.getY(), player.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(player.getX() - 2, player.getY() - 2, player.getPlane()));
				
				RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(player.getX(), player.getY() + 2, player.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(player.getX(), player.getY() - 2, player.getPlane()));
				
				RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(player.getX() + 1, player.getY() + 1, player.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(player.getX() + 1, player.getY() - 1, player.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(player.getX() - 1, player.getY() + 1, player.getPlane()));
				RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(player.getX() - 1, player.getY() - 1, player.getPlane()));
			}
		});
	}
	
	public void handleCombatDeflection(Actor source, Hit hit) {
		if (hit.getSplat() == HitSplat.MAGIC_DAMAGE) {
			if (usingPrayer(0, 17)) {
				hit.setDamage((int) (hit.getDamage() * source.getMagePrayerMultiplier()));
			} else if (usingPrayer(1, 7)) {
				int deflectedDamage = (int) (hit.getDamage() * 0.1);
				hit.setDamage((int) (hit.getDamage() * source.getMagePrayerMultiplier()));
				if (deflectedDamage > 0) {
					source.applyHit(new Hit(player, deflectedDamage, HitSplat.REFLECTED_DAMAGE));
					player.setNextGraphics(new Graphics(2228));
					player.setNextAnimation(new Animation(12573));
				}
			}
		} else if (hit.getSplat() == HitSplat.RANGE_DAMAGE) {
			if (usingPrayer(0, 18)) {
				hit.setDamage((int) (hit.getDamage() * source.getRangePrayerMultiplier()));
			} else if (usingPrayer(1, 8)) {
				int deflectedDamage = (int) (hit.getDamage() * 0.1);
				hit.setDamage((int) (hit.getDamage() * source.getRangePrayerMultiplier()));
				if (deflectedDamage > 0) {
					source.applyHit(new Hit(player, deflectedDamage, HitSplat.REFLECTED_DAMAGE));
					player.setNextGraphics(new Graphics(2229));
					player.setNextAnimation(new Animation(12573));
				}
			}
		} else if (hit.getSplat() == HitSplat.MELEE_DAMAGE) {
			if (usingPrayer(0, 19)) {
				hit.setDamage((int) (hit.getDamage() * source.getMeleePrayerMultiplier()));
			} else if (usingPrayer(1, 9)) {
				int deflectedDamage = (int) (hit.getDamage() * 0.1);
				hit.setDamage((int) (hit.getDamage() * source.getMeleePrayerMultiplier()));
				if (deflectedDamage > 0) {
					source.applyHit(new Hit(player, deflectedDamage, HitSplat.REFLECTED_DAMAGE));
					player.setNextGraphics(new Graphics(2230));
					player.setNextAnimation(new Animation(12573));
				}
			}
		}
	}
	
	public void handleCurseBoosts(Player p2, Hit hit) {
		
		if (p2.getPrayer().hasPrayersOn()) {
			if (p2.getPrayer().usingPrayer(0, 24)) { // smite
				int drain = hit.getDamage() / 4;
				if (drain > 0) {
					drainPrayer(drain);
				}
			} else {
				if (p2.getPrayer().usingPrayer(1, 18)) {
					sendSoulSplit(hit, p2);
				}
				if (hit.getDamage() == 0) {
					return;
				}
				if (!p2.getPrayer().isBoostedLeech()) {
					if (hit.getSplat() == HitSplat.MELEE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 19)) {
							if (Misc.getRandom(4) == 0) {
								p2.getPrayer().increaseTurmoilBonus(player);
								p2.getPrayer().setBoostedLeech(true);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 1)) { // sap att
							if (Misc.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(0)) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
								} else {
									p2.getPrayer().increaseLeechBonus(0);
									p2.getPackets().sendGameMessage("Your curse drains Attack from the enemy, boosting your Attack.", true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2214));
								p2.getPrayer().setBoostedLeech(true);
								RegionManager.sendProjectile(p2, player, 2215, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.setNextGraphics(new Graphics(2216));
									}
								}, 1);
								return;
							}
						} else {
							if (p2.getPrayer().usingPrayer(1, 10)) {
								if (Misc.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(3)) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
									} else {
										p2.getPrayer().increaseLeechBonus(3);
										p2.getPackets().sendGameMessage("Your curse drains Attack from the enemy, boosting your Attack.", true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									RegionManager.sendProjectile(p2, player, 2231, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											player.setNextGraphics(new Graphics(2232));
										}
									}, 1);
									return;
								}
							}
							if (p2.getPrayer().usingPrayer(1, 14)) {
								if (Misc.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(7)) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
									} else {
										p2.getPrayer().increaseLeechBonus(7);
										p2.getPackets().sendGameMessage("Your curse drains Strength from the enemy, boosting your Strength.", true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									RegionManager.sendProjectile(p2, player, 2248, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											player.setNextGraphics(new Graphics(2250));
										}
									}, 1);
									return;
								}
							}
							
						}
					}
					if (hit.getSplat() == HitSplat.RANGE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 2)) { // sap range
							if (Misc.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(1)) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
								} else {
									p2.getPrayer().increaseLeechBonus(1);
									p2.getPackets().sendGameMessage("Your curse drains Range from the enemy, boosting your Range.", true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2217));
								p2.getPrayer().setBoostedLeech(true);
								RegionManager.sendProjectile(p2, player, 2218, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.setNextGraphics(new Graphics(2219));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 11)) {
							if (Misc.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(4)) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
								} else {
									p2.getPrayer().increaseLeechBonus(4);
									p2.getPackets().sendGameMessage("Your curse drains Range from the enemy, boosting your Range.", true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								RegionManager.sendProjectile(p2, player, 2236, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.setNextGraphics(new Graphics(2238));
									}
								});
								return;
							}
						}
					}
					if (hit.getSplat() == HitSplat.MAGIC_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 3)) { // sap mage
							if (Misc.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(2)) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
								} else {
									p2.getPrayer().increaseLeechBonus(2);
									p2.getPackets().sendGameMessage("Your curse drains Magic from the enemy, boosting your Magic.", true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2220));
								p2.getPrayer().setBoostedLeech(true);
								RegionManager.sendProjectile(p2, player, 2221, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.setNextGraphics(new Graphics(2222));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 12)) {
							if (Misc.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(5)) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
								} else {
									p2.getPrayer().increaseLeechBonus(5);
									p2.getPackets().sendGameMessage("Your curse drains Magic from the enemy, boosting your Magic.", true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								RegionManager.sendProjectile(p2, player, 2240, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.setNextGraphics(new Graphics(2242));
									}
								}, 1);
								return;
							}
						}
					}
					
					// overall
					
					if (p2.getPrayer().usingPrayer(1, 13)) { // leech defence
						if (Misc.getRandom(10) == 0) {
							if (p2.getPrayer().reachedMax(6)) {
								p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
							} else {
								p2.getPrayer().increaseLeechBonus(6);
								p2.getPackets().sendGameMessage("Your curse drains Defence from the enemy, boosting your Defence.", true);
							}
							p2.setNextAnimation(new Animation(12575));
							p2.getPrayer().setBoostedLeech(true);
							RegionManager.sendProjectile(p2, player, 2244, 35, 35, 20, 5, 0, 0);
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									player.setNextGraphics(new Graphics(2246));
								}
							}, 1);
							return;
						}
					}
					
					if (p2.getPrayer().usingPrayer(1, 15)) {
						if (Misc.getRandom(10) == 0) {
							if (player.getAttributes().getRunEnergy() <= 0) {
								p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
							} else {
								p2.getAttributes().setRunEnergy(p2.getAttributes().getRunEnergy() > 90 ? 100 : p2.getAttributes().getRunEnergy() + 10);
								player.getAttributes().setRunEnergy(p2.getAttributes().getRunEnergy() > 10 ? player.getAttributes().getRunEnergy() - 10 : 0);
							}
							p2.setNextAnimation(new Animation(12575));
							p2.getPrayer().setBoostedLeech(true);
							RegionManager.sendProjectile(p2, player, 2256, 35, 35, 20, 5, 0, 0);
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									player.setNextGraphics(new Graphics(2258));
								}
							}, 1);
							return;
						}
					}
					
					if (p2.getPrayer().usingPrayer(1, 16)) {
						if (Misc.getRandom(10) == 0) {
							if (player.getCombatDefinitions().getSpecialAttackPercentage() <= 0) {
								p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
							} else {
								p2.getCombatDefinitions().restoreSpecialAttack();
								player.getCombatDefinitions().decreaseSpecialEnergy(10);
							}
							p2.setNextAnimation(new Animation(12575));
							p2.getPrayer().setBoostedLeech(true);
							RegionManager.sendProjectile(p2, player, 2252, 35, 35, 20, 5, 0, 0);
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									player.setNextGraphics(new Graphics(2254));
								}
							}, 1);
							return;
						}
					}
					
					if (p2.getPrayer().usingPrayer(1, 4)) { // sap spec
						if (Misc.getRandom(10) == 0) {
							p2.setNextAnimation(new Animation(12569));
							p2.setNextGraphics(new Graphics(2223));
							p2.getPrayer().setBoostedLeech(true);
							if (player.getCombatDefinitions().getSpecialAttackPercentage() <= 0) {
								p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
							} else {
								player.getCombatDefinitions().decreaseSpecialEnergy(10);
							}
							RegionManager.sendProjectile(p2, player, 2224, 35, 35, 20, 5, 0, 0);
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									player.setNextGraphics(new Graphics(2225));
								}
							}, 1);
						}
					}
				}
			}
		}
	}
	
	public void sendSoulSplit(final Hit hit, final Actor user) {
		final Player target = player;
		if (hit.getDamage() > 0) {
			RegionManager.sendProjectile(user, player, 2263, 11, 11, 20, 5, 0, 0);
		}
		user.heal(hit.getDamage() / 5);
		drainPrayer(hit.getDamage() / 5);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextGraphics(new Graphics(2264));
				if (hit.getDamage() > 0) {
					RegionManager.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0, 0);
				}
			}
		}, 1);
	}
	
	public boolean isBoostedLeech() {
		return boostedLeech;
	}
	
	public void increaseTurmoilBonus(Player p2) {
		leechBonuses[8] = (int) ((100 * Math.floor(0.15 * p2.getSkills().getLevelForXp(SkillConstants.ATTACK))) / p2.getSkills().getLevelForXp(SkillConstants.ATTACK));
		leechBonuses[9] = (int) ((100 * Math.floor(0.15 * p2.getSkills().getLevelForXp(SkillConstants.DEFENCE))) / p2.getSkills().getLevelForXp(SkillConstants.DEFENCE));
		leechBonuses[10] = (int) ((100 * Math.floor(0.1 * p2.getSkills().getLevelForXp(SkillConstants.STRENGTH))) / p2.getSkills().getLevelForXp(SkillConstants.STRENGTH));
		adjustStat(0, leechBonuses[8]);
		adjustStat(1, leechBonuses[10]);
		adjustStat(2, leechBonuses[9]);
	}
	
	public boolean reachedMax(int bonus) {
		if (bonus != 8 && bonus != 9 && bonus != 10) {
			return leechBonuses[bonus] >= 20;
		} else {
			return false;
		}
	}
	
	public void increaseLeechBonus(int bonus) {
		leechBonuses[bonus]++;
		if (bonus == 0) {
			adjustStat(0, leechBonuses[bonus]);
			adjustStat(1, leechBonuses[bonus]);
			adjustStat(2, leechBonuses[bonus]);
		} else if (bonus == 1) {
			adjustStat(2, leechBonuses[bonus]);
			adjustStat(3, leechBonuses[bonus]);
		} else if (bonus == 2) {
			adjustStat(2, leechBonuses[bonus]);
			adjustStat(4, leechBonuses[bonus]);
		} else if (bonus == 3) {
			adjustStat(0, leechBonuses[bonus]);
		} else if (bonus == 4) {
			adjustStat(3, leechBonuses[bonus]);
		} else if (bonus == 5) {
			adjustStat(4, leechBonuses[bonus]);
		} else if (bonus == 6) {
			adjustStat(2, leechBonuses[bonus]);
		} else if (bonus == 7) {
			adjustStat(1, leechBonuses[bonus]);
		}
	}
	
	public void setBoostedLeech(boolean boostedLeech) {
		this.boostedLeech = boostedLeech;
	}
}

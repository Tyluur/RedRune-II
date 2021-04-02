package org.redrune.game.entity.actor.player.data;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.SkillConstants;
import org.redrune.utility.functions.Misc;

import java.io.Serializable;

public final class PlayerSkills implements Serializable, SkillConstants {
	
	private static final long serialVersionUID = -7086829989489745985L;
	
	public short level[];
	
	private double xp[];
	
	private double xpCounter;
	
	private boolean[] enabledSkillsTargets;
	
	private boolean[] skillsTargetsUsingLevelMode;
	
	private int[] skillsTargetsValues;
	
	private transient Player player;

	public double[] getXp() {
		return xp;
	}
	
	public PlayerSkills() {
		level = new short[25];
		xp = new double[25];
		for (int i = 0; i < level.length; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
		level[HERBLORE] = 3;
		xp[HERBLORE] = 250;
		enabledSkillsTargets = new boolean[25];
		skillsTargetsUsingLevelMode = new boolean[25];
		skillsTargetsValues = new int[25];
	}
	
	public void passLevels(Player p) {
		this.level = p.getSkills().level;
		this.xp = p.getSkills().xp;
	}
	
	public void restoreSkills() {
		for (int skill = 0; skill < level.length; skill++) {
			level[skill] = (short) getLevelForXp(skill);
			refresh(skill);
		}
	}
	
	public int getLevelForXp(int skill) {
		double exp = xp[skill];
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= (skill == DUNGEONEERING ? 120 : 99); lvl++) {
			points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return skill == DUNGEONEERING ? 120 : 99;
	}
	
	public void refresh(int skill) {
		player.getPackets().sendSkillLevel(skill);
		player.getAppearance().generateAppearanceData();
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public double getXp(int skill) {
		return xp[skill];
	}
	
	/**
	 * Drains a skill level with a cap on it
	 *
	 * @param skill
	 * 		The skill id to drain
	 * @param drainAmount
	 * 		The amount to drain
	 * @param drainCap
	 * 		The amount we are capped by
	 */
	public void drainLevel(int skill, double drainAmount, double drainCap) {
		int skillLevel = level[skill];
		int levelForXp = getLevelForXp(skill);
		int lowestAllowed = levelForXp - (int) Math.round(levelForXp * drainCap);
		// can no longer drain past this
		if (skillLevel <= lowestAllowed) {
			return;
		}
		int drain = (int) Math.round(levelForXp * drainAmount);
		drainLevel(skill, drain);
	}
	
	/**
	 * Drains a level
	 *
	 * @param skill
	 * 		The skill
	 * @param drain
	 * 		The amount to drain
	 */
	public int drainLevel(int skill, int drain) {
		int drainLeft = drain - level[skill];
		if (drainLeft < 0) {
			drainLeft = 0;
		}
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		refresh(skill);
		return drainLeft;
	}
	
	public int getCombatLevelWithSummoning() {
		return getCombatLevel() + getSummoningCombatLevel();
	}
	
	public int getCombatLevel() {
		int attack = getLevelForXp(0);
		int defence = getLevelForXp(1);
		int strength = getLevelForXp(2);
		int hp = getLevelForXp(3);
		int prayer = getLevelForXp(5);
		int ranged = getLevelForXp(4);
		int magic = getLevelForXp(6);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;
		double melee = (attack + strength) * 0.325;
		double ranger = Math.floor(ranged * 1.5) * 0.325;
		double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		return combatLevel;
	}
	
	public int getSummoningCombatLevel() {
		return getLevelForXp(SUMMONING) / 8;
	}
	
	public void drainSummoning(int amt) {
		int level = getLevel(SUMMONING);
		if (level == 0) {
			return;
		}
		set(SUMMONING, amt > level ? 0 : level - amt);
	}
	
	public int getLevel(int skill) {
		return level[skill];
	}
	
	public void set(int skill, int newLevel) {
		level[skill] = (short) newLevel;
		refresh(skill);
	}
	
	public void init() {
		for (int skill = 0; skill < level.length; skill++) {
			refresh(skill);
		}
		refreshEnabledSkillsTargets();
		refreshUsingLevelTargets();
		refreshSkillsTargetsValues();
		refreshXpCounter();
	}
	
	private void refreshXpCounter() {
		player.getPackets().sendConfig(1801, (int) (xpCounter * 10));
	}
	
	public void resetXpCounter() {
		xpCounter = 0;
		refreshXpCounter();
	}
	
	public void addXpNoModifier(int skill, double exp) {
		if (player.getAttributes().isExperienceLocked()) {
			return;
		}
		trackExperienceChange(skill, exp);
	}
	
	public void addXp(int skill, double exp) {
		if (player.getAttributes().isExperienceLocked()) {
			return;
		}
		trackExperienceChange(skill, exp);
	}
	
	private void trackExperienceChange(int skill, double exp) {
		player.getControllerManager().trackXP(skill, (int) exp);
		int oldLevel = getLevelForXp(skill);
		xp[skill] += exp;
		xpCounter += exp;
		refreshXpCounter();
		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			player.getDialogueManager().startDialogue("LevelUp", skill);
			if (skill == HITPOINTS) {
				player.heal(levelDiff * 10);
			}
			if (skill == PRAYER) {
				player.getPrayer().restorePrayer(levelDiff * 10);
			}
			if (skill == SUMMONING || skill <= MAGIC) {
				player.getAppearance().generateAppearanceData();
			}
		}
		refresh(skill);
	}
	
	public boolean isMaxed() {
		int maxlevels = 0;
		for (int ji = 0; ji < level.length; ji++) {
			if (this.getLevel(ji) != 99) {
				continue;
			}
			maxlevels++;
		}
		return maxlevels >= 23;
	}
	
	public void addSkillXpRefresh(int skill, double xp) {
		this.xp[skill] += xp;
		level[skill] = (short) getLevelForXp(skill);
	}
	
	public void resetSkillNoRefresh(int skill) {
		xp[skill] = 0;
		level[skill] = 1;
	}
	
	public boolean NumberToSkill(int number) {
		int found = 0;
		for (int i = 0; i < 24; i++) {
			if (getLevel(i) >= 99) {
				found++;
			}
		}
		return found >= number;
		
	}
	
	public void setXp(int skill, double exp) {
		xp[skill] = exp;
		refresh(skill);
	}
	
	public int getTargetIdByComponentId(int componentId) {
		switch (componentId) {
			case 200: // Attack
				return 0;
			case 11: // Strength
				return 1;
			case 52: // Range
				return 2;
			case 93: // Magic
				return 3;
			case 28: // Defence
				return 4;
			case 193: // Constitution
				return 5;
			case 76: // Prayer
				return 6;
			case 19: // Agility
				return 7;
			case 36: // Herblore
				return 8;
			case 60: // Theiving
				return 9;
			case 84: // Crafting
				return 10;
			case 110: // Runecrafting
				return 11;
			case 186: // Mining
				return 12;
			case 179: // Smithing
				return 13;
			case 44: // Fishing
				return 14;
			case 68: // Cooking
				return 15;
			case 172: // Firemaking
				return 16;
			case 165: // Woodcutting
				return 17;
			case 101: // Fletching
				return 18;
			case 118: // Slayer
				return 19;
			case 126: // Farming
				return 20;
			case 134: // Construction
				return 21;
			case 142: // Hunter
				return 22;
			case 150: // Summoning
				return 23;
			case 158: // Dungeoneering
				return 24;
			default:
				return -1;
		}
	}
	
	public int getSkillIdByTargetId(int targetId) {
		switch (targetId) {
			case 0: // Attack
				return ATTACK;
			case 1: // Strength
				return STRENGTH;
			case 2: // Range
				return RANGE;
			case 3: // Magic
				return MAGIC;
			case 4: // Defence
				return DEFENCE;
			case 5: // Constitution
				return HITPOINTS;
			case 6: // Prayer
				return PRAYER;
			case 7: // Agility
				return AGILITY;
			case 8: // Herblore
				return HERBLORE;
			case 9: // Thieving
				return THIEVING;
			case 10: // Crafting
				return CRAFTING;
			case 11: // Runecrafting
				return RUNECRAFTING;
			case 12: // Mining
				return MINING;
			case 13: // Smithing
				return SMITHING;
			case 14: // Fishing
				return FISHING;
			case 15: // Cooking
				return COOKING;
			case 16: // Firemaking
				return FIREMAKING;
			case 17: // Woodcutting
				return WOODCUTTING;
			case 18: // Fletching
				return FLETCHING;
			case 19: // Slayer
				return SLAYER;
			case 20: // Farming
				return FARMING;
			case 21: // Construction
				return CONSTRUCTION;
			case 22: // Hunter
				return HUNTER;
			case 23: // Summoning
				return SUMMONING;
			case 24: // Dungeoneering
				return DUNGEONEERING;
			default:
				return -1;
		}
	}
	
	public void refreshEnabledSkillsTargets() {
		int value = Misc.get32BitValue(enabledSkillsTargets, true);
		player.getPackets().sendConfig(1966, value);
	}
	
	public void refreshUsingLevelTargets() {
		int value = Misc.get32BitValue(skillsTargetsUsingLevelMode, true);
		player.getPackets().sendConfig(1968, value);
	}
	
	public void refreshSkillsTargetsValues() {
		for (int i = 0; i < 25; i++) {
			player.getPackets().sendConfig(1969 + i, skillsTargetsValues[i]);
		}
	}
	
	public void setSkillTargetEnabled(int id, boolean enabled) {
		enabledSkillsTargets[id] = enabled;
		refreshEnabledSkillsTargets();
	}
	
	public void setSkillTargetUsingLevelMode(int id, boolean using) {
		skillsTargetsUsingLevelMode[id] = using;
		refreshUsingLevelTargets();
	}
	
	public void setSkillTargetValue(int skillId, int value) {
		skillsTargetsValues[skillId] = value;
		refreshSkillsTargetsValues();
	}
	
	public void setSkillTarget(boolean usingLevel, int skillId, int target) {
		setSkillTargetEnabled(skillId, true);
		setSkillTargetUsingLevelMode(skillId, usingLevel);
		setSkillTargetValue(skillId, target);
		
	}

	public void refreshAllSkills() {
		for (int skill = 0; skill < level.length; skill++) {
			refresh(skill);
		}
	}

}

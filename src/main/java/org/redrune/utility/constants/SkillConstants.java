package org.redrune.utility.constants;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
public interface SkillConstants {
	
	double MAXIMUM_EXP = 200000000;
	
	int ATTACK = 0;
	
	int DEFENCE = 1;
	
	int STRENGTH = 2;
	
	int HITPOINTS = 3;
	
	int RANGE = 4;
	
	int PRAYER = 5;
	
	int MAGIC = 6;
	
	int COOKING = 7;
	
	int WOODCUTTING = 8;
	
	int FLETCHING = 9;
	
	int FISHING = 10;
	
	int FIREMAKING = 11;
	
	int CRAFTING = 12;
	
	int SMITHING = 13;
	
	int MINING = 14;
	
	int HERBLORE = 15;
	
	int AGILITY = 16;
	
	int THIEVING = 17;
	
	int SLAYER = 18;
	
	int FARMING = 19;
	
	int RUNECRAFTING = 20;
	
	int CONSTRUCTION = 22;
	
	int HUNTER = 21;
	
	int SUMMONING = 23;
	
	int DUNGEONEERING = 24;
	
	String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Hitpoints", "Range", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Construction", "Hunter", "Summoning", "Dungeoneering" };
	
	static int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}
}

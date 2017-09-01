package com.rs.game.content.node.item;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.PlayerInventory;
import com.rs.game.entity.actor.player.data.PlayerSkills;

/**
 * @author Raghav/Own4g3 <Raghav_ftw@hotmail.com>
 * @author Gircat <gircat101@gmail.com> Modified on Jul 29, 2014 at 10:58:28 PM.
 */
public class AncientEffigies {
	
	public static final int STARVED_ANCIENT_EFFIGY = 18778, NOURISHED_ANCIENT_EFFIGY = 18779, SATED_ANCIENT_EFFIGY = 18780, GORGED_ANCIENT_EFFIGY = 18781, DRAGONKIN_LAMP = 18782;
	
	public static int[] SKILL_1 = { PlayerSkills.AGILITY, PlayerSkills.CONSTRUCTION, PlayerSkills.COOKING, PlayerSkills.FISHING, PlayerSkills.FLETCHING, PlayerSkills.HERBLORE, PlayerSkills.MINING, PlayerSkills.SUMMONING };
	
	public static int[] SKILL_2 = { PlayerSkills.CRAFTING, PlayerSkills.THIEVING, PlayerSkills.FIREMAKING, PlayerSkills.FARMING, PlayerSkills.WOODCUTTING, PlayerSkills.HUNTER, PlayerSkills.SMITHING, PlayerSkills.RUNECRAFTING };
	
	public static int getRequiredLevel(int id) {
		switch (id) {
			case STARVED_ANCIENT_EFFIGY:
				return 91;
			case NOURISHED_ANCIENT_EFFIGY:
				return 93;
			case SATED_ANCIENT_EFFIGY:
				return 95;
			case GORGED_ANCIENT_EFFIGY:
				return 97;
		}
		return -1;
	}
	
	public static String getMessage(int skill) {
		switch (skill) {
			case PlayerSkills.AGILITY:
				return "deftness and precision";
			case PlayerSkills.CONSTRUCTION:
				return "buildings and security";
			case PlayerSkills.COOKING:
				return "fire and preparation";
			case PlayerSkills.FISHING:
				return "life and cultivation";
			case PlayerSkills.FLETCHING:
				return "lumber and woodworking";
			case PlayerSkills.HERBLORE:
				return "flora and fuana";
			case PlayerSkills.MINING:
				return "metalwork and minerals";
			case PlayerSkills.SUMMONING:
				return "binding essence and spirits";
		}
		return null;
	}
	
	public static int getExp(int itemId) {
		switch (itemId) {
			case STARVED_ANCIENT_EFFIGY:
				return 15000;
			case NOURISHED_ANCIENT_EFFIGY:
				return 20000;
			case SATED_ANCIENT_EFFIGY:
				return 25000;
			case GORGED_ANCIENT_EFFIGY:
				return 30000;
		}
		return -1;
	}
	
	public static void effigyInvestigation(Player player, int id) {
		PlayerInventory inv = player.getInventory();
		inv.deleteItem(id, 1);
		if (inv.containsOneItem(STARVED_ANCIENT_EFFIGY)) {
			inv.addItem(NOURISHED_ANCIENT_EFFIGY, 1);
		} else if (inv.containsOneItem(NOURISHED_ANCIENT_EFFIGY)) {
			inv.addItem(SATED_ANCIENT_EFFIGY, 1);
		} else if (inv.containsOneItem(SATED_ANCIENT_EFFIGY)) {
			inv.addItem(GORGED_ANCIENT_EFFIGY, 1);
		} else if (inv.containsOneItem(GORGED_ANCIENT_EFFIGY)) {
			inv.addItem(DRAGONKIN_LAMP, 1);
		}
	}
}

package org.redrune.game.content.entity.actor.player.skills.agility;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.SkillConstants;

/**
 * This class handles all agility preconditions
 *
 * @author Tyluur <itstyluur@gmail.com>
 */
public class Agility {
	
	/**
	 * Checks if a player has the agility level
	 *
	 * @param player
	 * 		The player
	 * @param level
	 * 		The level
	 */
	public static boolean hasLevel(Player player, int level) {
		if (player.getSkills().getLevel(SkillConstants.AGILITY) < level) {
			player.getPackets().sendMessage("You need an agility level of " + level + " to use this obstacle.", true);
			return false;
		}
		return true;
	}
	
}

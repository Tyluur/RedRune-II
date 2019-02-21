package org.redrune.utility.constants;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
public interface InterfaceConstants {
	
	/**
	 * Sends the quest interface to the player with the parameterized title and list of messages. The messages will be
	 * formatted to never overlap one line, but to go to the next one if it passes the limit of characters on a line.
	 *
	 * @param player
	 * 		The player
	 * @param title
	 * 		The title of the quest interface
	 * @param messageList
	 * 		The list of messages to send. a {@code String} {@code Array} {@code Object}
	 */
	static void sendQuestScroll(Player player, String title, String... messageList) {
		final int interfaceId = 275;
		final int endLine = 309;
		player.closeInterfaces();
		Misc.clearInterface(player, interfaceId);
		
		int startLine = 16;
		for (String message : messageList) {
			if (startLine > endLine) {
				break;
			}
			player.getPackets().sendIComponentText(interfaceId, startLine, message);
			startLine++;
		}
		
		player.getPackets().sendRunScript(1207, messageList.length);
		player.getPackets().sendIComponentText(interfaceId, 2, title);
		player.getInterfaceManager().sendInterface(interfaceId);
	}
}

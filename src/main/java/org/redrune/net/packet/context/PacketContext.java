package org.redrune.net.packet.context;

import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public abstract class PacketContext {
	
	/**
	 * Handles the packet context
	 *
	 * @param player
	 * 		The player to handle it for
	 */
	public abstract void handle(Player player);
	
}

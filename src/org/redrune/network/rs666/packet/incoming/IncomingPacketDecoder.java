package org.redrune.network.rs666.packet.incoming;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public interface IncomingPacketDecoder {
	
	/**
	 * The packet ids that bind the incoming packet
	 *
	 * @return A {@code Integer} array
	 */
	int[] bindings();
	
	/**
	 * Handles the reading of a packet
	 *
	 * @param player
	 * 		The player reading the packet
	 * @param packet
	 * 		The packet being read
	 */
	void read(Player player, Packet packet);
	
	/**
	 * Converts a varargs parameter over to an integer array
	 *
	 * @param arguments
	 * 		The arguments
	 */
	default int[] arguments(int... arguments) {
		return Misc.arguments(arguments);
	}
	
}

package org.redrune.network.rs666.packet.structure;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.rs2.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public interface IncomingPacketStructure {
	
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
	
}

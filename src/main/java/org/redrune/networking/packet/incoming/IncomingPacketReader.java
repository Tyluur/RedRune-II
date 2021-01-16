package org.redrune.networking.packet.incoming;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.utility.constants.PacketConstants;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public interface IncomingPacketReader extends PacketConstants {
	
	/**
	 * The packet ids that are bound to this reader
	 */
	int[] bindings();
	
	/**
	 * Reads a packet
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	PacketContext read(Player player, Packet packet);
	
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

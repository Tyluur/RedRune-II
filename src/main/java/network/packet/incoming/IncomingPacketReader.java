package network.packet.incoming;

import game.entity.actor.player.Player;
import network.packet.Packet;
import network.packet.context.PacketContext;
import utility.constants.PacketConstants;
import utility.functions.Misc;

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

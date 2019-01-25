package org.redrune.networking.codec.packet;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.constants.PacketConstants;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public interface IncomingPacketDecoder extends PacketConstants {
	
	/**
	 * The packet ids that bind the incoming packet
	 *
	 * @return A {@code Integer} array
	 */
	int[] bindings();
	
	/**
	 * Handling the decoding of the packet received
	 *
	 * @param player
	 * 		The player whose session received the packet
	 * @param stream
	 * 		The stream of the packet
	 * @param packetId
	 * 		The id of the packet
	 * @param packetLength
	 * 		The length of the packet
	 */
	void decode(Player player, InputStream stream, int packetId, int packetLength);
	
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

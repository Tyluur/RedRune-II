package org.redrune.networking.codec.packet;

import org.redrune.game.GameFlags;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.functions.Misc;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class IncomingPacketRepository {
	
	/**
	 * The map of {@code IncomingPacketDecoder}s
	 */
	private static final ConcurrentHashMap<Integer, IncomingPacketDecoder> PACKET_MAP = new ConcurrentHashMap<>();
	
	/**
	 * Initializes all decoders
	 */
	public static void initialize() {
		Misc.getClassesInDirectory(IncomingPacketRepository.class.getPackage().getName() + ".impl").stream().filter(IncomingPacketDecoder.class::isInstance).forEach((clazz) -> include((IncomingPacketDecoder) clazz));
		System.out.println("Initialized " + PACKET_MAP.size() + " incoming packet decoders");
	}
	
	/**
	 * Includes the decoder
	 *
	 * @param decoder
	 * 		The decoder instance
	 */
	private static void include(IncomingPacketDecoder decoder) {
		Arrays.stream(decoder.bindings()).forEach(key -> {
			if (PACKET_MAP.containsKey(key)) {
				throw new IllegalStateException("Defined incoming packet [" + PACKET_MAP.get(key) + "] #" + key + " already and attempted to store " + decoder + " ahead of it.");
			}
			PACKET_MAP.put(key, decoder);
		});
	}
	
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
	public static void handlePacket(Player player, InputStream stream, int packetId, int packetLength) {
		try {
			IncomingPacketDecoder decoder = PACKET_MAP.get(packetId);
			if (decoder == null) {
				System.out.println("Received game packet #" + packetId + ", unidentified handler.");
				return;
			}
			decoder.decode(player, stream, packetId, packetLength);
		/*	if (GameFlags.debugMode) {
				System.out.println("Handled packet #" + packetId + " with decoder " + decoder.getClass().getSimpleName());
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

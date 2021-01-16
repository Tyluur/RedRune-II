package org.redrune.networking.packet.incoming;

import org.redrune.game.GameFlags;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.utility.functions.Misc;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class IncomingPacketRepository {
	
	/**
	 * The map of {@code IncomingPacketDecoder}s
	 */
	private static final ConcurrentHashMap<Integer, IncomingPacketReader> PACKET_MAP = new ConcurrentHashMap<>();
	
	/**
	 * Initializes all decoders
	 */
	public static void initialize() {
		Misc.getClasses(IncomingPacketRepository.class.getPackage().getName() + ".impl").stream().filter(IncomingPacketReader.class::isInstance).forEach((clazz) -> include((IncomingPacketReader) clazz));
		System.out.println("Initialized " + PACKET_MAP.size() + " incoming packet decoders");
	}
	
	/**
	 * Includes the decoder
	 *
	 * @param decoder
	 * 		The decoder instance
	 */
	private static void include(IncomingPacketReader decoder) {
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
	 * @param packet
	 * 		The packet that will be read
	 */
	public static void handlePacket(Player player, Packet packet) {
		try {
			int packetId = packet.getOpcode();
			IncomingPacketReader reader = PACKET_MAP.get(packetId);
			if (reader == null) {
				System.out.println("Received game packet #" + packetId + ", unidentified handler.");
				return;
			}
			PacketContext context = reader.read(player, packet);
			if (context == null) {
				if (GameFlags.debugMode) {
					System.out.println("Unable to get packet context for incoming packet " + packet + " with [reader=" + reader.getClass().getSimpleName() + "]!");
				}
				return;
			}
			player.getSession().addContext(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

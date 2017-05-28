package org.redrune.network.rs666.packet.structure;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.Misc;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class IncomingPacketRepository {
	
	/**
	 * The logger instance
	 */
	private static final Logger logger = Misc.constructLogger(IncomingPacketRepository.class);
	
	/**
	 * The map of {@code IncoingPacketStructure}s
	 */
	private static final ConcurrentHashMap<Integer, IncomingPacketStructure> STREAM_DECODER_MAP = new ConcurrentHashMap<>();
	
	/**
	 * Stores all {@code IncoingPacketStructure}s
	 */
	public static void storeAll() {
		Misc.getClassesInDirectory(IncomingPacketRepository.class.getPackage().getName() + ".in").stream().filter(IncomingPacketStructure.class::isInstance).forEach((clazz) -> {
			IncomingPacketStructure decoder = (IncomingPacketStructure) clazz;
			Arrays.stream(decoder.bindings()).forEach(key -> STREAM_DECODER_MAP.put(key, decoder));
		});
		logger.info("Number of incoming packets that are handled: " + STREAM_DECODER_MAP.size());
	}
	
	/**
	 * Handles an incoming packet
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	public static void handlePacket(Player player, Packet packet) {
		try {
			final int opcode = packet.getOpcode();
			IncomingPacketStructure structure = STREAM_DECODER_MAP.get(opcode);
			if (structure == null) {
				System.out.println("Received packet " + opcode + ", unidentified handler.");
				return;
			}
			structure.read(player, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
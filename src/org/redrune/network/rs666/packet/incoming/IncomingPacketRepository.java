package org.redrune.network.rs666.packet.incoming;

import org.redrune.core.EngineWorkingSet;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.utility.tool.Misc;

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
	private static final Logger LOGGER = Misc.constructLogger(IncomingPacketRepository.class);
	
	/**
	 * The map of {@code IncoingPacketStructure}s
	 */
	private static final ConcurrentHashMap<Integer, IncomingPacketDecoder> DECODER_MAP = new ConcurrentHashMap<>();
	
	/**
	 * Stores all {@code IncoingPacketStructure}s
	 */
	public static void storeAll() {
		Misc.getClassesInDirectory(IncomingPacketRepository.class.getPackage().getName() + ".impl").stream().filter(IncomingPacketDecoder.class::isInstance).forEach((clazz) -> {
			IncomingPacketDecoder decoder = (IncomingPacketDecoder) clazz;
			Arrays.stream(decoder.bindings()).forEach(key -> DECODER_MAP.put(key, decoder));
		});
		LOGGER.info("Number of incoming packets that are handled: " + DECODER_MAP.size());
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
		final int opcode = packet.getOpcode();
		// packet decoding is done instantly but on a separate worker
		EngineWorkingSet.executePacketWork(() -> {
			try {
				IncomingPacketDecoder structure = DECODER_MAP.get(opcode);
				if (structure == null) {
					System.out.println("Received packet " + opcode + ", unidentified handler.");
					return;
				}
				structure.read(player, packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
}
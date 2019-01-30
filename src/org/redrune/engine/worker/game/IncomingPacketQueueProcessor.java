package org.redrune.engine.worker.game;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.networking.Session;
import org.redrune.networking.codec.packet.IncomingPacketRepository;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.functions.Misc;

import static org.redrune.utility.constants.PacketConstants.PACKET_SIZES;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-29
 */
public class IncomingPacketQueueProcessor implements Runnable {
	
	/**
	 * The object to synchronize packet reading through
	 */
	private final Object lock = new Object();
	
	@Override
	public void run() {
		synchronized (lock) {
			try {
				for (Player player : World.getLobbyPlayers()) {
					if (player == null) {
						continue;
					}
					if (player.getSession().getIncomingQueue().isEmpty()) {
						continue;
					}
					player.getSession().getIncomingQueue().removeIf(e -> {
						processStream(player, player.getSession(), e);
						return true;
					});
				}
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted()) {
						continue;
					}
					if (player.getSession().getIncomingQueue().isEmpty()) {
						continue;
					}
					player.getSession().getIncomingQueue().removeIf(e -> {
						processStream(player, player.getSession(), e);
						return true;
					});
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	/**
	 * Processes a stream
	 *
	 * @param player
	 * 		The player
	 * @param session
	 * 		The session of the stream
	 * @param stream
	 * 		The stream
	 */
	private void processStream(Player player, Session session, InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected() && !player.isFinished()) {
			int packetId = stream.readUnsignedByte();
			if (packetId >= PACKET_SIZES.length) {
				System.out.println("PacketId " + packetId + " has fake packet id.");
				break;
			}
			int length = PACKET_SIZES[packetId];
			if (length == -1) {
				length = stream.readUnsignedByte();
			} else if (length == -2) {
				length = stream.readUnsignedShort();
			} else if (length == -4) {
				length = stream.getRemaining();
				System.out.println("Unregistered packet size for packet # " + packetId + " - size guessed to be " + length);
			}
			if (length > stream.getRemaining()) {
				length = stream.getRemaining();
				System.out.println("Packet # " + packetId + " has fake size - expected size " + length);
			}
			int startOffset = stream.getOffset();
			processPackets(player, packetId, stream, length);
			stream.setOffset(startOffset + length);
		}
	}
	
	private void processPackets(Player player, final int packetId, InputStream stream, int length) {
		IncomingPacketRepository.handlePacket(player, stream, packetId, length);
		player.getAttributes().setPacketsDecoderPing(Misc.currentTimeMillis());
	}
}

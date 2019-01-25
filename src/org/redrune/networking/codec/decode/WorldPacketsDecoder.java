package org.redrune.networking.codec.decode;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.Session;
import org.redrune.networking.codec.Decoder;
import org.redrune.networking.codec.packet.IncomingPacketRepository;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.functions.Misc;

import static org.redrune.utility.constants.PacketConstants.*;

public final class WorldPacketsDecoder extends Decoder {
	
	static {
		loadPacketSizes();
	}
	
	private Player player;
	
	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}
	
	@Override
	public void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected() && !player.hasFinished()) {
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
			processPackets(packetId, stream, length);
			stream.setOffset(startOffset + length);
		}
	}
	
	private void processPackets(final int packetId, InputStream stream, int length) {
		IncomingPacketRepository.handlePacket(player, stream, packetId, length);
		player.setPacketsDecoderPing(Misc.currentTimeMillis());
	}
	
	public Player getPlayer() {
		return player;
	}
	
}

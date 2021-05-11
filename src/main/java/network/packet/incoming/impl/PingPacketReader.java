package network.packet.incoming.impl;

import game.entity.actor.player.Player;
import network.packet.Packet;
import network.packet.context.PacketContext;
import network.packet.context.impl.PingPacketContext;
import network.packet.incoming.IncomingPacketReader;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class PingPacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(PING_PACKET, PING_STATISTICS_PACKET);
	}
	
	@Override
	public PacketContext read(Player player, Packet packet) {
		int packetId = packet.getOpcode();
		if (packetId == PING_PACKET) {
			return new PingPacketContext(-1);
		} else {
			int ping = packet.readShort();
			return new PingPacketContext(ping);
		}
	}
}

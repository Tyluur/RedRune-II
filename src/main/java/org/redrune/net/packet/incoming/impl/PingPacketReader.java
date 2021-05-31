package org.redrune.net.packet.incoming.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.net.packet.Packet;
import org.redrune.net.packet.context.PacketContext;
import org.redrune.net.packet.context.impl.PingPacketContext;
import org.redrune.net.packet.incoming.IncomingPacketReader;

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

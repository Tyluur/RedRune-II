package org.redrune.networking.packet.incoming.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.context.impl.ClientDetailsPacketContext;
import org.redrune.networking.packet.incoming.IncomingPacketReader;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-04
 */
public class ClientDetailsEventPacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(RECEIVE_PACKET_COUNT_PACKET);
	}
	
	@Override
	public PacketContext read(Player player, Packet packet) {
		int count = packet.readInt();
		return new ClientDetailsPacketContext(count);
	}
}

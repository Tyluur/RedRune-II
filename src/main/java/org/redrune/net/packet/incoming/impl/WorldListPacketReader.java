package org.redrune.net.packet.incoming.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.net.packet.Packet;
import org.redrune.net.packet.context.PacketContext;
import org.redrune.net.packet.context.impl.WorldListRequestContext;
import org.redrune.net.packet.incoming.IncomingPacketReader;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class WorldListPacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(WORLD_LIST_REQUEST_PACKET);
	}
	
	@Override
	public PacketContext read(Player player, Packet packet) {
		int updateType = packet.readInt();
		return new WorldListRequestContext(updateType);
	}
	
}

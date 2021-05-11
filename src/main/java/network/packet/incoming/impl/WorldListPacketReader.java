package network.packet.incoming.impl;

import game.entity.actor.player.Player;
import network.packet.Packet;
import network.packet.context.PacketContext;
import network.packet.context.impl.WorldListRequestContext;
import network.packet.incoming.IncomingPacketReader;

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

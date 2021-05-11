package network.packet.incoming.impl;

import game.entity.actor.player.Player;
import network.packet.Packet;
import network.packet.context.PacketContext;
import network.packet.context.impl.WalkPacketContext;
import network.packet.incoming.IncomingPacketReader;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class WalkPacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(WALKING_PACKET, MINI_WALKING_PACKET);
	}
	
	@Override
	public PacketContext read(Player player, Packet packet) {
		int destX = packet.readUnsignedShortLE128();
		int destY = packet.readUnsignedShortLE128();
		boolean forceRun = packet.readByte() == 1;
		return new WalkPacketContext(destX, destY, forceRun);
	}
}

package org.redrune.networking.packet.incoming.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.context.impl.WalkPacketContext;
import org.redrune.networking.packet.incoming.IncomingPacketReader;

/**
 * @author Tyluur <itstyluur@gmail.com>
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

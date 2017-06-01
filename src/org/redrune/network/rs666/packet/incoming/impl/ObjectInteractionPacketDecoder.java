package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class ObjectInteractionPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(1, 39, 86, 58, 38, 75);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		int y = packet.readShortA();
		int x = packet.readLEShortA();
		int objectId = packet.readLEShort();
		boolean running = packet.readByte() == 1;
		
		System.out.println("Clicked object: " + x + ", " + y + ", " + objectId + ", " + running + ", " + packet.getOpcode());
	}
}

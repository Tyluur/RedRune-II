package org.redrune.network.rs666.packet.structure.in;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class ObjectInteractionPacketStructure implements IncomingPacketStructure {
	
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

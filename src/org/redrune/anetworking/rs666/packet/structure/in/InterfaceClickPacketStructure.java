package org.redrune.network.rs666.packet.structure.in;

import org.redrune.anetworking.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/22/2017
 */
public class InterfaceClickPacketStructure implements IncomingPacketStructure {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(85, 7, 66, 11, 48, 17, 84, 40, 25, 8);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		int clickData = packet.readLEInt();
		int interfaceId = clickData & 0xFFF;
		int componentId = clickData >> 16;
		int itemId = packet.readShortA();
		int slot = packet.readShortA();
		if (itemId == 65535) {
			itemId = -1;
		}
		if (slot == 65535) {
			slot = -1;
		}
	}
}

package org.redrune.network.rs666.packet.structure.in;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.Misc;
import org.redrune.utility.rs.constant.InterfaceConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class InterfaceSlotSwapPacketStructure implements IncomingPacketStructure {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(71);
	}
	
	@SuppressWarnings("unused")
	@Override
	public void read(Player player, Packet packet) {
		int fromItemId = packet.readShort();
		int toHash = packet.readInt2();
		int fromSlotId = packet.readShort();
		int toSlotId = packet.readShort();
		int toItemId = packet.readShort();
		int interfaceHash = packet.readInt();
		int fromInterfaceId = interfaceHash >> 16;
		int fromChild = interfaceHash & 0xff;
		int toChild = toHash & 0xFFFF;
		int toInterfaceId = toHash >> 16;
		
		if (fromInterfaceId == InterfaceConstants.INVENTORY_INTERFACE_ID) {
			toSlotId -= 28;
			if (toSlotId < 0 || toSlotId >= player.getInventory().getItems().getSize() || fromSlotId >= player.getInventory().getItems().getSize()) {
				return;
			}
			player.getInventory().switchItem(fromSlotId, toSlotId);
		}
	}
}

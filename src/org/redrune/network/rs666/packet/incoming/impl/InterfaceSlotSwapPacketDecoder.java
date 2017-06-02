package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.Misc;
import org.redrune.utility.rs.constant.InterfaceConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class InterfaceSlotSwapPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(71);
	}
	
	@SuppressWarnings("unused")
	@Override
	
	public void read(Player player, Packet packet) {
	/*	int fromItemId = packet.readShort();
		int toHash = packet.readInt2();
		int fromSlotId = packet.readShort();
		int toSlotId = packet.readShort();
		int toItemId = packet.readShort();
		int interfaceHash = packet.readInt();
		int fromInterfaceId = interfaceHash >> 16;
		int fromChild = interfaceHash & 0xff;
		int toChild = toHash & 0xFFFF;
		int toInterfaceId = toHash >> 16;*/
	
		int fromItemId = packet.readShort();
		int toHash = packet.readInt2();
		int fromSlot = packet.readShort();
		int toSlot = packet.readShort();
		int toItemId = packet.readShort();
		int interfaceHash = packet.readInt();
		
		int fromInterface = interfaceHash >> 16;
		int fromChild = interfaceHash & 0xff;
		int toChild = toHash & 0xFFFF;
		int toInterface = toHash >> 16;
		
//		System.out.println(fromItemId + "," + toHash + "," + fromSlot+ "," + toSlot + "," + toItemId +"," + interfaceHash + "," + fromInterface + "," + fromChild + "," + toChild + "," + toInterface);
		
		if (fromInterface == InterfaceConstants.INVENTORY_INTERFACE_ID && toInterface == InterfaceConstants.INVENTORY_INTERFACE_ID && toChild == 0) {
			toSlot -= 28;
			if (toSlot < 0 || toSlot >= player.getInventory().getItems().getSize() || fromSlot >= player.getInventory().getItems().getSize()) {
				return;
			}
			player.getInventory().switchItem(fromSlot, toSlot);
		} else if (fromInterface == 762) {
			player.getBank().switchItem(fromSlot, toSlot, toChild);
		}
	}
}

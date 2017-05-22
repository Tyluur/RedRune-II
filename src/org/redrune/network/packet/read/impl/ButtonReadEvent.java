package org.redrune.network.packet.read.impl;

import org.redrune.network.packet.read.PacketReadEvent;
import org.redrune.network.packet.read.impl.rsinterface.RSInterfaceRepository;
import org.redrune.network.stream.IoReadEvent;
import org.redrune.rs2.node.entity.player.Player;

public class ButtonReadEvent implements PacketReadEvent {

	@Override
	public void decodePacket(Player player, IoReadEvent packet) {
		int itemId = packet.readLEShortA();
		int slotId = packet.readShortA();
		int hash = packet.readIntB();
		int interfaceId = hash >> 16;
		int buttonId = hash & 0xff;
		if (slotId == 65535) {
			slotId = 0;
		}
		if (itemId == 65535) {
			itemId = 0;
		}
		System.out.println("InterfaceId: " + interfaceId + ", ButtonId: " + buttonId + ", " + "SlotId: " + slotId
				+ ", ItemId: " + itemId + ", Packet: " + packet.getPacketId());
		RSInterfaceRepository.handle(player, interfaceId, buttonId, slotId, itemId, packet.getPacketId());
	}

}

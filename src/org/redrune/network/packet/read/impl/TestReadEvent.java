package org.redrune.network.packet.read.impl;

import org.redrune.network.packet.read.PacketReadEvent;
import org.redrune.network.stream.IoReadEvent;
import org.redrune.rs2.node.entity.player.Player;

public class TestReadEvent implements PacketReadEvent {

	@Override
	public void decodePacket(Player player, IoReadEvent packet) {
		packet.readInt();
	}

}

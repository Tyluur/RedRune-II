package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.PlayerUpdatePacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;

@PacketHeader(packet = PacketType.VAR_SHORT)
public class PlayerUpdateWriteEvent implements PacketWriteEvent<PlayerUpdatePacket> {

	@Override
	public IoWriteEvent encodePacket(PlayerUpdatePacket context) {
		return null;
	}

}

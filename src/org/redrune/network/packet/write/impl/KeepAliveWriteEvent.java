package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.KeepAlivePacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;

@PacketHeader(packet = PacketType.STANDARD)
public class KeepAliveWriteEvent implements PacketWriteEvent<KeepAlivePacket> {

	@Override
	public IoWriteEvent encodePacket(KeepAlivePacket context) {
		return null;// Find the correct packet lel.
	}

}

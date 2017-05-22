package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.LocationPacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;

@PacketHeader(packet = PacketType.STANDARD)
public class LocationWriteEvent implements PacketWriteEvent<LocationPacket> {

	@Override
	public IoWriteEvent encodePacket(LocationPacket context) {
		IoWriteEvent buffer = IoWriteEvent.create(50);
		buffer.write(context.getLocation().getLocalX() >> 3)
				.writeC(context.getLocation().getLocalY() >> 3)
				.write(context.getLocation().getZ());
		return buffer;
	}

}

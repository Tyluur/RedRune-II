package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.GlobalConfigPacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;

@PacketHeader(packet = PacketType.STANDARD)
public class GlobalConfigWriteEvent implements PacketWriteEvent<GlobalConfigPacket> {

	@Override
	public IoWriteEvent encodePacket(GlobalConfigPacket context) {
		IoWriteEvent buffer = null;
		if (context.getValue() <= Byte.MIN_VALUE || context.getValue() >= Byte.MAX_VALUE) {
			buffer = IoWriteEvent.create(2);
			buffer.writeInt(context.getValue()).writeShort128(context.getId());
		} else {
			buffer = IoWriteEvent.create(137);
			buffer.writeShortLE128(context.getId()).writeS(context.getValue());
		}
		return buffer;
	}

}

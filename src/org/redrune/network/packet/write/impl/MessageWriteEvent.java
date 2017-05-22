package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.MessagePacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;

@PacketHeader(packet = PacketType.VAR_BYTE)
public class MessageWriteEvent implements PacketWriteEvent<MessagePacket> {

	@Override
	public IoWriteEvent encodePacket(MessagePacket context) {
		IoWriteEvent buffer = IoWriteEvent.create(53);
		return buffer.writeSmart(context.getChannel()).writeInt(0).write(0x0).writeRS2String(context.getMessage());
	}

}

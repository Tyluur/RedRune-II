package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.InterfacePacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;

/**
 * InterfaceWriteEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
@PacketHeader(packet = PacketType.STANDARD)
public class InterfaceWriteEvent implements PacketWriteEvent<InterfacePacket> {

	@Override
	public IoWriteEvent encodePacket(InterfacePacket context) {
		IoWriteEvent buffer = IoWriteEvent.create(139);
		return buffer.writeS(context.isWalkable() ? 1: 0).writeShort128(context.getInterfaceId()).writeInt(context.getPaneId() << 16 | context.getChildId());
//				.writeLEInt(0).writeS(context.isWalkable() ? 1 : 0).writeShortLE128(context.getInterfaceId())
//				.writeLEInt(0).writeIntA(0).writeIntB(0).writeIntB(context.getPaneId() << 16 | context.getChildId());
	}

}

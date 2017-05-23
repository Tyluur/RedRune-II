package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.SkillLevelPacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;

@PacketHeader(packet = PacketType.STANDARD)
public class SkillLevelWriteEvent implements PacketWriteEvent<SkillLevelPacket> {
	
	@Override
	public IoWriteEvent encodePacket(SkillLevelPacket context) {
		IoWriteEvent buffer = IoWriteEvent.create(8);
		buffer.writeByteC(context.getLevel());
		buffer.write(context.getSkill());
		buffer.writeInt2(context.getExperience());
		return buffer;
	}
	
}

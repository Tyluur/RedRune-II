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
		IoWriteEvent buffer = IoWriteEvent.create(133);
		return buffer.writeS(context.getSkill())
				.writeIntA((int) context.getExperience()).writeA(context.getLevel());
	}

}

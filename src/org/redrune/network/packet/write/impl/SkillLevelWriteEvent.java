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
		return buffer.writeS(context.getSkills().getSkill().ordinal())
				.writeIntA((int) context.getSkills().getExperience()).writeA(context.getSkills().getLevel());
	}

}

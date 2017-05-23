package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.WorldListPacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;
import org.redrune.rs2.world.connection.WorldDefinition;
import org.redrune.rs2.world.connection.WorldList;

/**
 * WorldListWriteEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
@PacketHeader(packet = PacketType.VAR_SHORT)
public class WorldListWriteEvent implements PacketWriteEvent<WorldListPacket> {

	@Override
	public IoWriteEvent encodePacket(WorldListPacket context) {
		IoWriteEvent buffer = IoWriteEvent.create(22);
		
		buffer.write(1);
		buffer.write(2);
		buffer.write(context.isUpdate() ? 1 : 0);
		if (context.isUpdate()) {
			buffer.writeSmart(WorldList.getWorldList().size());
			for (WorldDefinition world : WorldList.getWorldList()) {
				buffer.writeSmart(world.getCountry());
				buffer.writeGJString2(world.getRegion());
			}
			buffer.writeSmart(0);
			buffer.writeSmart(WorldList.getWorldList().size() + 1);
			buffer.writeSmart(WorldList.getWorldList().size());
			for (WorldDefinition world : WorldList.getWorldList()) {
				buffer.writeSmart(world.getWorldId());
				buffer.write(0);
				buffer.writeInt(world.getFlag());
				buffer.writeGJString2(world.getActivity());
				buffer.writeGJString2(world.getIp());
			}
			buffer.writeInt(0x94DA4A87);
		}
		for (WorldDefinition world : WorldList.getWorldList()) {
			buffer.writeSmart(world.getWorldId());
			buffer.writeShort(1337);
		}
		return buffer;
	}

}

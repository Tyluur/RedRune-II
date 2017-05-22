package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.WorldListPacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;
import org.redrune.rs2.world.connection.LobbyWorld;
import org.redrune.rs2.world.connection.WorldRepository;

/**
 * WorldListWriteEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
@PacketHeader(packet = PacketType.VAR_SHORT)
public class WorldListWriteEvent implements PacketWriteEvent<WorldListPacket> {

	@Override
	public IoWriteEvent encodePacket(WorldListPacket context) {
		IoWriteEvent buffer = IoWriteEvent.create(39);
		buffer.write(1);
		buffer.write(2);
		buffer.write(context.isUpdate() ? 1 : 0);
		if (context.isUpdate()) {
			buffer.writeSmart(WorldRepository.getWorlds().size());
			for (LobbyWorld world : WorldRepository.getWorlds()) {
				buffer.writeSmart(world.getCountryId());
				buffer.writeGJString2(world.getWorldActivity());
			}
			buffer.writeSmart(0);
			buffer.writeSmart(WorldRepository.getWorlds().size() + 1);
			buffer.writeSmart(WorldRepository.getWorlds().size());
			for (LobbyWorld world : WorldRepository.getWorlds()) {
				buffer.writeSmart(world.getWorldId());
				buffer.write(0);
				buffer.writeInt(world.isMembersOnly() ? world.isLootshareEnabled() ? 0x1 | 0x8 : 0x1 : 0x8);
				buffer.writeGJString2(world.getWorldActivity());
				buffer.writeGJString2(world.getWorldAddress());
			}
			buffer.writeInt(0x94DA4A87);
		}
		for (LobbyWorld world : WorldRepository.getWorlds()) {
			buffer.writeSmart(world.getWorldId());
			buffer.writeShort(2000);
		}
		return buffer;
	}

}

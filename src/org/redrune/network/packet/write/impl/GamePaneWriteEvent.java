package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.GamePanePacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;

@PacketHeader(packet = PacketType.STANDARD)
public class GamePaneWriteEvent implements PacketWriteEvent<GamePanePacket> {

	@Override
	public IoWriteEvent encodePacket(GamePanePacket context) {
		IoWriteEvent buffer = IoWriteEvent.create(148);
		return buffer.writeInt(0).writeInt(0).writeShort128(context.getPaneId()).writeIntB(0)
				.writeC(context.getPaneType()).writeIntA(0);
	}

}

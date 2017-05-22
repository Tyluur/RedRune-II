package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.LogoutPacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;

@PacketHeader(packet = PacketType.STANDARD)
public class LogoutWriteEvent implements PacketWriteEvent<LogoutPacket> {

	@Override
	public IoWriteEvent encodePacket(LogoutPacket context) {
		IoWriteEvent buffer = IoWriteEvent.create(context.isToLobby() ? 142 : 60);
		context.getPlayer().sendLogout(context.isToLobby());
		return buffer;
	}

}

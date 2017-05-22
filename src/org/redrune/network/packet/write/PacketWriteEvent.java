package org.redrune.network.packet.write;

import org.redrune.network.packet.event.PacketEvent;
import org.redrune.network.stream.IoWriteEvent;

public interface PacketWriteEvent<P extends PacketEvent> {

	public IoWriteEvent encodePacket(P context);

}

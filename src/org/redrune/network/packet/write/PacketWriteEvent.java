package org.redrune.network.packet.write;

import org.redrune.network.packet.event.PacketContext;
import org.redrune.network.stream.IoWriteEvent;

public interface PacketWriteEvent<P extends PacketContext> {

	public IoWriteEvent encodePacket(P context);

}

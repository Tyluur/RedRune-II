package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.RunEnergyPacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;

@PacketHeader(packet = PacketType.STANDARD)
public class RunEnergyWriteEvent implements PacketWriteEvent<RunEnergyPacket> {

	@Override
	public IoWriteEvent encodePacket(RunEnergyPacket context) {
		IoWriteEvent buffer = IoWriteEvent.create(13);
		return buffer.write((int) context.getPlayer().getPropertiesManager().getRunEnergy());
	}

}

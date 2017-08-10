package org.redrune.network.master.server.network.packet.in;

import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.network.master.server.network.packet.out.StatusReceivePacketOut;
import org.redrune.network.master.server.world.MSRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/9/2017
 */
@Readable(packetIds = { PacketConstants.STATUS_UPDATE_PACKET_ID })
public class StatusUpdatePacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		String username = packet.readString();
		boolean online = packet.readByte() == 1;
		byte status = (byte) packet.readByte();
		byte worldId = (byte) packet.readByte();
		
		MSRepository.sendToAllSessions(new StatusReceivePacketOut(username, online, status, worldId));
	}
}

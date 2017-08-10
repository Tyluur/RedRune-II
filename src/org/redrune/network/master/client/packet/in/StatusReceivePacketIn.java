package org.redrune.network.master.client.packet.in;

import org.redrune.network.master.client.MasterCommunication;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/9/2017
 */
@Readable(packetIds = { PacketConstants.STATUS_RECEIVE_PACKET_ID })
public class StatusReceivePacketIn implements ReadablePacket<MCSession> {
	
	@Override
	public void read(MCSession session, IncomingPacket packet) {
		String username = packet.readString();
		boolean online = packet.readByte() == 1;
		byte status = (byte) packet.readByte();
		byte worldId = (byte) packet.readByte();
		
		MasterCommunication.read(packet.getId(), username, online, status, worldId);
	}
}

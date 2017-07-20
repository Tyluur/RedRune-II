package org.redrune.network.master.client.packet.in;

import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.client.MasterCommunication;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
@Readable(packetIds = { PacketConstants.LOGIN_RESPONSE_PACKET_ID })
public class LoginResponsePacketIn implements ReadablePacket<MCSession> {
	
	@Override
	public void read(MCSession session, IncomingPacket packet) {
		// decode first
		String uuid = packet.readString();
		byte responseCode = (byte) packet.readByte();
		boolean lobby = (byte) packet.readByte() == 1;
		String fileText = packet.readString();
		String username = packet.readString();
		
		// handle the reading now
		MasterCommunication.read(packet.getId(), uuid, username, fileText, responseCode, lobby);
	}
}

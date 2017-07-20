package org.redrune.network.master.server.network.packet.in;

import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.network.master.utility.Utility;
import org.redrune.network.master.utility.rs.LoginConstants;

import java.io.File;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
@Readable(packetIds = { PacketConstants.PLAYER_FILE_UPDATE_PACKET_ID })
public class PlayerFilePacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		String fileName = packet.readString();
		String fileContents = packet.readString();
		
		Utility.saveData(new File(LoginConstants.getLocation(fileName)), fileContents);
	}
}

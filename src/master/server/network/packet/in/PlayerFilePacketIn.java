package master.server.network.packet.in;

import master.network.packet.IncomingPacket;
import master.network.packet.PacketConstants;
import master.network.packet.readable.Readable;
import master.network.packet.readable.ReadablePacket;
import master.server.network.MSSession;
import master.utility.Utility;
import master.utility.rs.LoginConstants;

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

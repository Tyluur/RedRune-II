package master.server.network.packet.in;

import master.network.packet.IncomingPacket;
import master.network.packet.PacketConstants;
import master.network.packet.readable.Readable;
import master.network.packet.readable.ReadablePacket;
import master.server.engine.MSEngineFactory;
import master.server.network.MSSession;
import master.utility.rs.LoginRequest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
@Readable(packetIds = { PacketConstants.LOGIN_REQUEST_PACKET_ID })
public class LoginRequestPacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		byte worldId = (byte) packet.readByte();
		boolean lobby = packet.readByte() == 1;
		String username = packet.readString();
		String password = packet.readString();
		String uuid = packet.readString();
		
		// adds a login request
		MSEngineFactory.addLoginRequest(new LoginRequest(worldId, lobby, username, password, session, uuid));
	}
}

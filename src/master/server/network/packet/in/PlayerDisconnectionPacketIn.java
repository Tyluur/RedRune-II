package master.server.network.packet.in;

import master.network.packet.IncomingPacket;
import master.network.packet.PacketConstants;
import master.network.packet.readable.Readable;
import master.network.packet.readable.ReadablePacket;
import master.server.network.MSSession;
import master.server.world.MSRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
@Readable(packetIds = { PacketConstants.PLAYER_DISCONNECTION_PACKET_ID })
public class PlayerDisconnectionPacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		byte worldId = (byte) packet.readByte();
		boolean lobby = packet.readByte() == 1;
		String username = packet.readString();
		
		MSRepository.getWorld(worldId).ifPresent(world -> world.removePlayer(username, lobby));
	}
}

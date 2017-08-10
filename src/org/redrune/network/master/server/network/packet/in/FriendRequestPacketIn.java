package org.redrune.network.master.server.network.packet.in;

import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.network.master.server.network.packet.out.FriendDetailsPacketOut;
import org.redrune.network.master.server.world.MSRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/9/2017
 */
@Readable(packetIds = { PacketConstants.FRIEND_REQUEST_PACKET_ID })
public class FriendRequestPacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		String requester = packet.readString();
		byte requesterWorldId = (byte) packet.readByte();
		String requested = packet.readString();
		
		// the details about the requested player
		Object[] details = MSRepository.getPlayerDetails(requested);
		
		// writes the packet back to the world the requester was on
		MSRepository.getWorld(requesterWorldId).ifPresent(world -> world.getSession().write(new FriendDetailsPacketOut(requester, (String) details[0], (boolean) details[1], (byte) details[2])));
	}
}

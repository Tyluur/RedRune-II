package master.server.network.packet.in;

import master.network.packet.IncomingPacket;
import master.network.packet.PacketConstants;
import master.network.packet.readable.Readable;
import master.network.packet.readable.ReadablePacket;
import master.server.network.MSSession;
import master.server.network.packet.out.PrivateMessageDeliveryPacketOut;
import master.server.network.packet.out.PrivateMessageReceivePacketOut;
import master.server.world.MSRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
@Readable(packetIds = { PacketConstants.PRIVATE_MESSAGE_ATTEMPT_PACKET_ID })
public class PrivateMessageAttemptPacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		String fromName = packet.readString();
		byte fromRights = (byte) packet.readByte();
		String toName = packet.readString();
		String message = packet.readString();
		
		// writes the message because we found a user by that name
		MSRepository.getSessionByUsername(fromName).ifPresent(client -> client.write(new PrivateMessageDeliveryPacketOut(fromName, toName, message)));
		MSRepository.getSessionByUsername(toName).ifPresent(client -> client.write(new PrivateMessageReceivePacketOut(fromName, fromRights, toName, message)));
	}
}

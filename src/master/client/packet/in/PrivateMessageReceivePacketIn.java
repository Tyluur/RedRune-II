package master.client.packet.in;

import master.client.network.MCSession;
import master.network.packet.IncomingPacket;
import master.network.packet.PacketConstants;
import master.network.packet.readable.Readable;
import master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.MasterCommunication;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
@Readable(packetIds = { PacketConstants.PRIVATE_MESSAGE_RECEIVE_PACKET_ID })
public class PrivateMessageReceivePacketIn implements ReadablePacket<MCSession> {
	
	@Override
	public void read(MCSession session, IncomingPacket packet) {
		String fromName = packet.readString();
		byte fromRights = (byte) packet.readByte();
		String toName = packet.readString();
		String message = packet.readString();
		
		MasterCommunication.read(packet.getId(), fromName, fromRights, toName, message);
	}
}

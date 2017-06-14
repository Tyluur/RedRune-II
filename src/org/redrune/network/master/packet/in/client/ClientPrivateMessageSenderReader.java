package org.redrune.network.master.packet.in.client;

import org.jboss.netty.channel.Channel;
import org.redrune.network.RS2MasterCommunication;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ClientPrivateMessageSenderReader implements MasterPacketReader {
	
	@Override
	public int getId() {
		return SERVER_PRIVATE_MESSAGE_SENDER_PACKET;
	}
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		long uid = packet.readLong();
		String fromUsername = packet.readString();
		packet.readInt();
		String toUsername = packet.readString();
		String message = packet.readString();
		boolean successful = packet.readBoolean();
		
		RS2MasterCommunication.readPacket(uid, packet, fromUsername, toUsername, message, successful);
	}
}

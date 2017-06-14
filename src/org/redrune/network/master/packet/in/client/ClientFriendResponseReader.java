package org.redrune.network.master.packet.in.client;

import org.jboss.netty.channel.Channel;
import org.redrune.network.RS2MasterCommunication;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public class ClientFriendResponseReader implements MasterPacketReader {
	
	@Override
	public int getId() {
		return SERVER_FRIEND_DATA_PACKET;
	}
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		long uid = packet.readLong();
		String name = packet.readString();
		int response = packet.readInt() - 2;
		
		RS2MasterCommunication.readPacket(uid, packet, name, response);
	}
}

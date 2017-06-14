package org.redrune.network.master.packet.in.client;

import org.jboss.netty.channel.Channel;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.network.master.client.MasterClientHandler;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ClientVerificationReader implements MasterPacketReader {
	
	@Override
	public int getId() {
		return SERVER_VERIFICATION_PACKET;
	}
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		String text = packet.readString();
		System.out.println(text);
		
		MasterClientHandler.setVerified(true);
	}
}

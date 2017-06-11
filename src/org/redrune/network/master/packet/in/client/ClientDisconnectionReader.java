package org.redrune.network.master.packet.in.client;

import org.jboss.netty.channel.Channel;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.network.master.server.MasterServerHandler;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class ClientDisconnectionReader implements MasterPacketReader {
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		packet.readLong(); // uid, unused
		boolean lobby = packet.readBoolean();
		String username = packet.readString();
		
		if (lobby) {
			MasterServerHandler.getRepository().removeLobbyPlayer(username);
		} else {
			MasterServerHandler.getRepository().removeWorldPlayer(username);
		}
	}
}

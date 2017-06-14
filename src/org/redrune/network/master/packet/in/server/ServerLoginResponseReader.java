package org.redrune.network.master.packet.in.server;

import org.jboss.netty.channel.Channel;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.network.master.server.MasterServerHandler;
import org.redrune.network.master.server.login.MasterServerLogin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class ServerLoginResponseReader implements MasterPacketReader {
	
	@Override
	public int getId() {
		return LOGIN_INFORMATION_CLIENT_PACKET;
	}
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		long uid = packet.readLong();
		String username = packet.readString();
		String password = packet.readString();
		boolean lobbyConnection = packet.readBoolean();
		int worldId = packet.readByte();
		
		// now we dump it to the login worker
		MasterServerHandler.getRepository().getUpdateWorker().addLogin(new MasterServerLogin(channel, uid, username, password, lobbyConnection, worldId));
	}
}

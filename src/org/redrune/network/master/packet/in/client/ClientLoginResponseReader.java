package org.redrune.network.master.packet.in.client;

import org.jboss.netty.channel.Channel;
import org.redrune.core.task.impl.MasterServerTasks;
import org.redrune.core.task.context.LoginData;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public final class ClientLoginResponseReader implements MasterPacketReader {
	
	@Override
	public int getId() {
		return LOGIN_INFORMATION_SERVER_PACKET;
	}
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		long uid = packet.readLong();
		String username = packet.readString();
		String password = packet.readString();
		boolean lobbyConnection = packet.readBoolean();
		int code = packet.readByte();
		String fileText = packet.readString();
		
		MasterServerTasks.addLogin(new LoginData(uid, username, password, code, lobbyConnection, fileText));
	}
}

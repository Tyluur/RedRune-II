package org.redrune.network.master.packet.in.server;

import org.jboss.netty.channel.Channel;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.network.master.packet.out.server.build.ServerLoginResponseBuilder;
import org.redrune.network.master.packet.out.server.context.ServerLoginResponseContext;
import org.redrune.network.master.server.MasterServerHandler;
import org.redrune.utility.Misc;
import org.redrune.utility.backend.ReturnCode;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class ServerLoginResponseReader implements MasterPacketReader {
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		long uid = packet.readLong();
		String username = packet.readString();
		String password = packet.readString();
		boolean lobbyConnection = packet.readByte() == 1;
		int worldId = packet.readByte();
		
		ReturnCode code;
		
		if (Misc.invalidAccountName(username) || username.length() < 3 || password.length() >= 30) {
			code = ReturnCode.INVALID_CREDENTIALS;
		} else if (MasterServerHandler.getRepository().userLoggedIn(username, lobbyConnection)) {
			code = ReturnCode.ALREADY_ONLINE;
		} else {
			code = ReturnCode.SUCCESSFUL;
		}
		
		if (code == ReturnCode.SUCCESSFUL) {
			if (lobbyConnection) {
				// just incase we weren't able to add the player.
				if (!MasterServerHandler.getRepository().addLobbyPlayer(uid, username)) {
					code = ReturnCode.ALREADY_ONLINE;
				}
			} else {
				// failsafe
				if (!MasterServerHandler.getRepository().addWorldPlayer(uid, username, worldId)) {
					code = ReturnCode.ALREADY_ONLINE;
				}
			}
			
			if (lobbyConnection) {
				MasterServerHandler.getRepository().removeWorldPlayer(username);
			} else {
				MasterServerHandler.getRepository().removeLobbyPlayer(username);
			}
		}
		
		
		channel.write(new ServerLoginResponseBuilder(new ServerLoginResponseContext(uid, username, password, lobbyConnection, code.getValue())).build());
	}
}

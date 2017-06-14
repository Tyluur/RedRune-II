package org.redrune.network.master.packet.in.server;

import org.jboss.netty.channel.Channel;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.network.master.packet.out.server.build.ServerPlayerLogoutBuilder;
import org.redrune.network.master.packet.out.server.context.ServerPlayerLogoutContext;
import org.redrune.network.master.server.MasterServerHandler;
import org.redrune.network.master.server.login.MasterServerLogin;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class ServerClientDisconnectionReader implements MasterPacketReader {
	
	@Override
	public int getId() {
		return DISCONNECTION_CLIENT_PACKET;
	}
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		packet.readLong(); // uid, unused
		boolean lobby = packet.readBoolean();
		String username = packet.readString();
		String data = packet.readString();
		
		if (lobby) {
			MasterServerHandler.getRepository().removeLobbyPlayer(username);
		} else {
			MasterServerHandler.getRepository().removeWorldPlayer(username);
		}
		
		Player player = Misc.constructPlayer(data);
		if (player == null) {
			System.out.println("Unable to construct player from received data... not saving.");
			return;
		}
		MasterServerLogin.savePlayer(player);
		MasterServerHandler.getRepository().writeToAllWorlds(new ServerPlayerLogoutBuilder(new ServerPlayerLogoutContext(username, lobby)).build());
	}
}

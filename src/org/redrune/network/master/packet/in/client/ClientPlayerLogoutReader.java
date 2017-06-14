package org.redrune.network.master.packet.in.client;

import org.jboss.netty.channel.Channel;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.RS2MasterCommunication;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.network.rs666.NetworkSession;
import org.redrune.network.rs666.packet.outgoing.impl.FriendsListBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ClientPlayerLogoutReader implements MasterPacketReader {
	
	@Override
	public int getId() {
		return SERVER_PLAYER_LOGOUT_PACKET;
	}
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		String username = packet.readString();
		boolean lobby = packet.readBoolean();
		
		// that player just logged out...
		
		for (NetworkSession networkSession : RS2MasterCommunication.getActiveSessions()) {
			Player networkPlayer = networkSession.getPlayer();
			if (networkPlayer.getManager().getContacts().hasFriend(username)) {
				networkPlayer.getTransmitter().send(new FriendsListBuilder(username).build(null));
			}
		}
	}
}

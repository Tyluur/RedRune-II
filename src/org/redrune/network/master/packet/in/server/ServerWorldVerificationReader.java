package org.redrune.network.master.packet.in.server;

import org.jboss.netty.channel.Channel;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.network.master.packet.out.server.build.ServerVerificationBuilder;
import org.redrune.network.master.packet.out.server.context.ServerVerificationContext;
import org.redrune.network.master.server.MasterServerHandler;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ServerWorldVerificationReader implements MasterPacketReader {
	
	@Override
	public int getId() {
		return CLIENT_VERIFICATION_PACKET;
	}
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		int worldId = packet.readInt();
		String password = packet.readString();
		
		if (!password.equals(PASSWORD)) {
			return;
		}
		if (MasterServerHandler.getRepository().isWorldVerified(worldId)) {
			return;
		}
		MasterServerHandler.getRepository().addWorld(worldId, channel);
		MasterServerHandler.getRepository().writeToWorld(worldId, new ServerVerificationBuilder(new ServerVerificationContext()).build());
	}
}

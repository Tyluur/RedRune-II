package org.redrune.network.master.packet.in.server;

import org.jboss.netty.channel.Channel;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.network.master.packet.out.server.build.ServerPrivateMessageBuilder;
import org.redrune.network.master.packet.out.server.context.ServerPrivateMessageContext;
import org.redrune.network.master.server.MasterServerHandler;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ServerPrivateMessageReader implements MasterPacketReader {
	
	@Override
	public int getId() {
		return CLIENT_PRIVATE_MESSAGE_PACKET;
	}
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		long uid = packet.readLong();
		String fromUsername = packet.readString();
		int fromRights = packet.readInt();
		String toUsername = packet.readString();
		String message = packet.readString();
		
		int toWorldId = MasterServerHandler.getRepository().getWorldId(toUsername);
		long toUid = MasterServerHandler.getRepository().getUid(toUsername);
		
		// making sure they will both receive da ting fam
		if (toUid != -1 && toWorldId != -1) {
			// send off the data
			channel.write(new ServerPrivateMessageBuilder(new ServerPrivateMessageContext(uid, fromUsername, fromRights, toUsername, message, true), true).build());
			
			MasterServerHandler.getRepository().writeToUser(toUsername, new ServerPrivateMessageBuilder(new ServerPrivateMessageContext(toUid, fromUsername, fromRights, toUsername, message, true), false).build());
		}
	}
}

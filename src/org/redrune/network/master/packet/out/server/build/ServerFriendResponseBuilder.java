package org.redrune.network.master.packet.out.server.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.server.context.ServerFriendResponseContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public class ServerFriendResponseBuilder extends MasterPacketBuilder<ServerFriendResponseContext> {
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public ServerFriendResponseBuilder(ServerFriendResponseContext context) {
		super(context);
	}
	
	@Override
	public MasterPacket build() {
		MasterPacket packet = new MasterPacket(SERVER_FRIEND_DATA_PACKET);
		packet.writeLong(context.getUid());
		packet.writeString(context.getName());
		packet.writeInt(context.getResponse() + 2);
		
		return packet;
	}
}

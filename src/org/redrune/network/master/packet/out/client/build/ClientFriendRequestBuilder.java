package org.redrune.network.master.packet.out.client.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.client.context.ClientFriendRequestContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public class ClientFriendRequestBuilder extends MasterPacketBuilder<ClientFriendRequestContext> {
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public ClientFriendRequestBuilder(ClientFriendRequestContext context) {
		super(context);
	}
	
	@Override
	public MasterPacket build() {
		MasterPacket packet = new MasterPacket(CLIENT_FRIEND_REQUEST_PACKET);
		packet.writeLong(context.getUid());
		packet.writeString(context.getName());
		return packet;
	}
}

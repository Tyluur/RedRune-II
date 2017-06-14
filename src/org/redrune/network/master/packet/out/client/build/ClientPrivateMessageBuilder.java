package org.redrune.network.master.packet.out.client.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.client.context.ClientPrivateMessageContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ClientPrivateMessageBuilder extends MasterPacketBuilder<ClientPrivateMessageContext> {
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public ClientPrivateMessageBuilder(ClientPrivateMessageContext context) {
		super(context);
	}
	
	@Override
	public MasterPacket build() {
		MasterPacket packet = new MasterPacket(CLIENT_PRIVATE_MESSAGE_PACKET);
		packet.writeLong(context.getUid());
		packet.writeString(context.getFromUsername());
		packet.writeInt(context.getFromRights());
		packet.writeString(context.getToUsername());
		packet.writeString(context.getMessage());
		return packet;
	}
}

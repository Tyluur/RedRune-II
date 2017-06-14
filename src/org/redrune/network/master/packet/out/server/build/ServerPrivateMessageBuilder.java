package org.redrune.network.master.packet.out.server.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.server.context.ServerPrivateMessageContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ServerPrivateMessageBuilder extends MasterPacketBuilder<ServerPrivateMessageContext> {
	
	/**
	 * The id of the message
	 */
	private static long messageId = 1;
	
	/**
	 * If we are sending the packet back to the sender
	 */
	private final boolean sender;
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 * @param sender
	 * 		If we are sending the packet back to the sender
	 */
	public ServerPrivateMessageBuilder(ServerPrivateMessageContext context, boolean sender) {
		super(context);
		this.sender = sender;
	}
	
	@Override
	public MasterPacket build() {
		MasterPacket packet = new MasterPacket(sender ? SERVER_PRIVATE_MESSAGE_SENDER_PACKET : SERVER_PRIVATE_MESSAGE_RECEIVER_PACKET);
		packet.writeLong(context.getUid());
		packet.writeString(context.getFromUsername());
		packet.writeInt(context.getFromRights());
		packet.writeString(context.getToUsername());
		packet.writeString(context.getMessage());
		packet.writeBoolean(context.isSuccessful());
		if (!sender) {
			packet.writeLong(messageId++);
		}
		return packet;
	}
}

package org.redrune.network.master.packet.out.client.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.client.context.ClientSessionDisconnectionContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class ClientSessionDisconnectionBuilder extends MasterPacketBuilder<ClientSessionDisconnectionContext> {
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public ClientSessionDisconnectionBuilder(ClientSessionDisconnectionContext context) {
		super(context);
	}
	
	@Override
	public MasterPacket build() {
		MasterPacket packet = new MasterPacket(DISCONNECTION_CLIENT_PACKET);
		packet.writeLong(context.getUid());
		packet.writeBoolean(context.isLobby());
		packet.writeString(context.getUsername());
		packet.writeString(context.getJsonText());
		return packet;
	}
}

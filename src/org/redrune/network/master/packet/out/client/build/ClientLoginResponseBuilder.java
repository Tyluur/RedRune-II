package org.redrune.network.master.packet.out.client.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.client.context.ClientLoginResponseContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class ClientLoginResponseBuilder extends MasterPacketBuilder<ClientLoginResponseContext> {
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public ClientLoginResponseBuilder(ClientLoginResponseContext context) {
		super(context);
	}
	
	@Override
	public MasterPacket build() {
		MasterPacket packet = new MasterPacket(LOGIN_INFORMATION_CLIENT_PACKET);
		packet.writeLong(context.getUid());
		packet.writeString(context.getUsername());
		packet.writeString(context.getPassword());
		packet.writeBoolean(context.isLobbyConnection());
		packet.writeByte(context.getWorldId());
		return packet;
	}
}

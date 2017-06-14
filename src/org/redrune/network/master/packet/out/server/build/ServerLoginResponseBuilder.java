package org.redrune.network.master.packet.out.server.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.server.context.ServerLoginResponseContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/9/2017
 */
public class ServerLoginResponseBuilder extends MasterPacketBuilder<ServerLoginResponseContext> {
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public ServerLoginResponseBuilder(ServerLoginResponseContext context) {
		super(context);
	}
	
	@Override
	public MasterPacket build() {
		MasterPacket packet = new MasterPacket(LOGIN_INFORMATION_SERVER_PACKET);
		packet.writeLong(context.getUid());
		packet.writeString(context.getUsername());
		packet.writeString(context.getPassword());
		packet.writeBoolean(context.isLobbyConnection());
		packet.writeByte(context.getResponseCode());
		packet.writeString(context.getFileJsonText());
		return packet;
	}
}

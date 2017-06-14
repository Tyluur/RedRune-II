package org.redrune.network.master.packet.out.server.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.server.context.ServerPlayerLogoutContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ServerPlayerLogoutBuilder extends MasterPacketBuilder<ServerPlayerLogoutContext> {
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public ServerPlayerLogoutBuilder(ServerPlayerLogoutContext context) {
		super(context);
	}
	
	@Override
	public MasterPacket build() {
		MasterPacket packet = new MasterPacket(SERVER_PLAYER_LOGOUT_PACKET);
		packet.writeString(context.getUsername());
		packet.writeBoolean(context.isLobby());
		return packet;
	}
}

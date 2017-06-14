package org.redrune.network.master.packet.out.server.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.server.context.ServerPlayerLoginContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ServerPlayerLoginBuilder extends MasterPacketBuilder<ServerPlayerLoginContext> {
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public ServerPlayerLoginBuilder(ServerPlayerLoginContext context) {
		super(context);
	}
	
	@Override
	public MasterPacket build() {
		MasterPacket packet = new MasterPacket(SERVER_PLAYER_LOGIN_PACKET);
		packet.writeString(context.getUsername());
		packet.writeByte(context.getWorldId() + 2);
		return packet;
	}
}

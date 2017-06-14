package org.redrune.network.master.packet.out.server.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.server.context.ServerVerificationContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ServerVerificationBuilder extends MasterPacketBuilder<ServerVerificationContext> {
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public ServerVerificationBuilder(ServerVerificationContext context) {
		super(context);
	}
	
	@Override
	public MasterPacket build() {
		final MasterPacket packet = new MasterPacket(SERVER_VERIFICATION_PACKET);
		packet.writeString("Successfully verified world");
		return packet;
	}
}

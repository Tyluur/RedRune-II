package org.redrune.network.master.packet.out.client.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.client.context.ClientVerificationPacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ClientVerificationPacketBuilder extends MasterPacketBuilder<ClientVerificationPacketContext> {
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public ClientVerificationPacketBuilder(ClientVerificationPacketContext context) {
		super(context);
	}
	
	@Override
	public MasterPacket build() {
		MasterPacket packet = new MasterPacket(CLIENT_VERIFICATION_PACKET);
		packet.writeInt(context.getWorldId());
		packet.writeString(context.getPassword());
		return packet;
	}
}

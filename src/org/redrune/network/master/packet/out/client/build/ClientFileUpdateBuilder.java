package org.redrune.network.master.packet.out.client.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.client.context.ClientFileUpdateContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
// TODO: implement file updating
public class ClientFileUpdateBuilder extends MasterPacketBuilder<ClientFileUpdateContext> {
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public ClientFileUpdateBuilder(ClientFileUpdateContext context) {
		super(context);
	}
	
	@Override
	public MasterPacket build() {
		MasterPacket packet = new MasterPacket(CLIENT_FILE_UPDATE_PACKET);
		packet.writeString(context.getUsername());
		packet.writeString(context.getJsonText());
		return packet;
	}
}

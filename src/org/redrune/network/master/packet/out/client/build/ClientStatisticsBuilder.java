package org.redrune.network.master.packet.out.client.build;

import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.packet.out.MasterPacketBuilder;
import org.redrune.network.master.packet.out.MasterPacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class ClientStatisticsBuilder extends MasterPacketBuilder<MasterPacketContext> {
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public ClientStatisticsBuilder(MasterPacketContext context) {
		super(context);
	}
	
	@Override
	public MasterPacket build() {
		return new MasterPacket(STATISTICS_REQUEST_CLIENT_PACKET);
	}
}

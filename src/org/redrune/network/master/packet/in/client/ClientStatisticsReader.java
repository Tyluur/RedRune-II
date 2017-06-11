package org.redrune.network.master.packet.in.client;

import org.jboss.netty.channel.Channel;
import org.redrune.core.master.client.MasterClientRepository;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class ClientStatisticsReader implements MasterPacketReader {
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		int worldCount = packet.readByte();
		for (int index = 1; index <= worldCount; index++) {
			int worldId = packet.readByte();
			int size = packet.readInt() - 1;
			boolean online = packet.readBoolean();
			MasterClientRepository.updateWorld(worldId, size, online);
		}
	}
}

package org.redrune.network.master.packet.in.server;

import org.jboss.netty.channel.Channel;
import org.redrune.core.master.server.MasterWorld;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.network.master.server.MasterServerHandler;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class ServerStatisticsRequestReader implements MasterPacketReader {
	
	@Override
	public void read(Channel channel, MasterPacket unusedPacket) {
		int size = MasterServerHandler.getRepository().getWorldCount();
		
		// now that we know we need to send stats, we will create the packet and send it
		MasterPacket outgoing = new MasterPacket(MasterConstants.STATISTICS_SEND_SERVER_PACKET);
		outgoing.writeByte(size);
		for (int worldId = 1; worldId <= size; worldId++) {
			MasterWorld world = MasterServerHandler.getRepository().getWorld(worldId);
			if (world == null) {
				System.err.println("Unable to find world #" + worldId);
				continue;
			}
			outgoing.writeByte(world.getWorldId());
			outgoing.writeInt(world.getPlayers().size() + 1);
			outgoing.writeBoolean(world.isOnline());
		}
		channel.write(outgoing);
	}
}

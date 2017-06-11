package org.redrune.network.master;

import org.jboss.netty.channel.Channel;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public interface MasterPacketReader {
	
	/**
	 * Handles the reading of the packet
	 *
	 * @param channel
	 * 		The channel The channel being read from
	 * @param packet
	 * 		The packet with data being read from
	 */
	void read(Channel channel, MasterPacket packet);
}

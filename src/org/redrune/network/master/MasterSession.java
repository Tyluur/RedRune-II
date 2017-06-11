package org.redrune.network.master;

import lombok.Getter;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class MasterSession {
	
	/**
	 * The channel of the session
	 */
	@Getter
	private final Channel channel;
	
	public MasterSession(Channel channel) {
		this.channel = channel;
	}
	
	/**
	 * Writes the packet to the channel
	 *
	 * @param packet
	 * 		The packet to write
	 */
	public synchronized ChannelFuture write(MasterPacket packet) {
		try {
			if (channel != null && channel.isConnected()) {
				return channel.write(packet);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
}

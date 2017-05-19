package org.redrune.network.rs666;

import lombok.Getter;
import lombok.Setter;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.rs2.node.entity.player.Player;

/**
 * The networkSession connected to the main game
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class NetworkSession {
	
	/**
	 * The channel instance.
	 */
	@Getter
	private Channel channel;
	
	/**
	 * The player affiliated with the networkSession
	 */
	@Getter
	@Setter
	private Player player;
	
	/**
	 * If the networkSession is in the lobby
	 */
	@Getter
	@Setter
	private boolean inLobby;
	
	public NetworkSession(Channel channel) {
		this.channel = channel;
	}
	
	/**
	 * Writes a packet to the channel
	 *
	 * @param packet
	 * 		The packet
	 */
	public ChannelFuture write(Packet packet) {
		if (channel != null && channel.isConnected()) {
			return channel.write(packet);
		}
		return null;
	}
	
}

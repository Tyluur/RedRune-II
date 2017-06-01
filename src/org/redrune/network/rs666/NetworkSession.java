package org.redrune.network.rs666;

import lombok.Getter;
import lombok.Setter;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerViewComponents;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.outgoing.impl.PingPacketBuilder;

/**
 * The networkSession connected to the main game
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class NetworkSession {
	
	/**
	 * The components of the players client
	 */
	@Getter
	private final PlayerViewComponents viewComponents;
	
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
	
	/**
	 * The ping count
	 */
	private byte pingCount;
	
	public NetworkSession(Channel channel) {
		this.channel = channel;
		this.viewComponents = new PlayerViewComponents();
	}
	
	/**
	 * Handles receiving a ping
	 */
	public void ping() {
		pingCount++;
		if (pingCount >= 5) {
			pingCount = 0;
			write(new PingPacketBuilder().build(null));
		}
	}
	
	/**
	 * Writes a packet to the channel
	 *
	 * @param packet
	 * 		The packet
	 */
	public synchronized ChannelFuture write(Packet packet) {
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

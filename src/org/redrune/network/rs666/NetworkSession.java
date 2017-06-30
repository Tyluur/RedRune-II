package org.redrune.network.rs666;

import lombok.Getter;
import lombok.Setter;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerViewComponents;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.outgoing.impl.PingPacketBuilder;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The networkSession connected to the main game
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class NetworkSession {
	
	/**
	 * The uid generator
	 */
	private static final AtomicLong UID_GENERATOR = new AtomicLong(1);
	
	/**
	 * The uid of the session
	 */
	@Getter
	private final long uid;
	
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
	 * If the networkSession is in the lobby
	 */
	@Getter
	@Setter
	private boolean inLobby;
	
	/**
	 * The ping count
	 */
	private byte pingCount;
	
	/**
	 * The queue of packets
	 */
	private ConcurrentLinkedQueue<Packet> packetQueue = new ConcurrentLinkedQueue<>();
	
	/**
	 * The player affiliated with the networkSession
	 */
	@Getter
	@Setter
	private Player player;
	
	public NetworkSession(Channel channel) {
		this.channel = channel;
		this.viewComponents = new PlayerViewComponents();
		// thread-safe uid generation. we'll never have more than the max long connections anyways
		this.uid = UID_GENERATOR.getAndIncrement();
	}
	
	@Override
	public String toString() {
		return "NetworkSession{" + "uid=" + uid + ", inLobby=" + inLobby + ", player=" + player + '}';
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
			if (player != null && player.isRenderable() && !player.getNetworkSession().isInLobby()) {
				packetQueue.add(packet);
				return null;
			} else {
				return writeNoDelay(packet);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Writes a packet with no delay
	 *
	 * @param packet
	 * 		The packet to write
	 */
	public synchronized ChannelFuture writeNoDelay(Packet packet) {
		if (channel != null && channel.isConnected()) {
			return channel.write(packet);
		}
		return null;
	}
	
	/**
	 * Flushes all of the packets
	 */
	public void flushPackets() {
		try {
			Packet packet;
			while ((packet = packetQueue.poll()) != null) {
				writeNoDelay(packet);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * Checking if the {@link #channel} is still active
	 */
	public boolean isActive() {
		return channel.isBound() && channel.isWritable() && channel.isReadable() && channel.isConnected();
	}
	
	/**
	 * Sycs the variables
	 *
	 * @param player
	 * 		The player
	 */
	public void sync(Player player) {
		setPlayer(player);
		player.setNetworkSession(this);
	}
}

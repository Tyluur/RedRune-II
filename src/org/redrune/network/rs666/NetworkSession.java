package org.redrune.network.rs666;

import lombok.Getter;
import lombok.Setter;
import master.client.packet.out.PlayerDisconnectionPacketOut;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.redrune.game.GameFlags;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerViewComponents;
import org.redrune.game.world.World;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.outgoing.impl.PingPacketBuilder;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The networkSession connected to the main game
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class NetworkSession {
	
	/**
	 * The mapping of uids
	 */
	private static final Map<String, NetworkSession> UID_MAP = new ConcurrentHashMap<>();
	
	/**
	 * The uuid of the session
	 */
	@Getter
	private final String uid;
	
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
		this.uid = generateCollisionSafeUuid().toString();
		
		connect();
	}
	
	/**
	 * Generates a collision-safe uuid. Although the chances of collision are insignificant we will be prepared for it.
	 */
	private UUID generateCollisionSafeUuid() {
		UUID uuid = UUID.randomUUID();
		while ((UID_MAP.containsKey(uuid.toString()))) {
			System.out.println("Generated a new uid [collision check]");
			uuid = UUID.randomUUID();
		}
		return uuid;
	}
	
	/**
	 * Handles the connection of a session
	 */
	private void connect() {
		UID_MAP.put(uid, this);
		System.out.println("NetworkSession.connect[" + uid + "]");
	}
	
	@Override
	public String toString() {
		return "NetworkSession{" + "uid=" + uid + ", inLobby=" + inLobby + ", player=" + player + '}';
	}
	
	/**
	 * Finds a session by its uid
	 *
	 * @param uid
	 * 		The uid of the session
	 */
	public static Optional<NetworkSession> findByUid(String uid) {
		for (Entry<String, NetworkSession> entry : UID_MAP.entrySet()) {
			String entryUid = entry.getKey();
			if (Objects.equals(entryUid, uid)) {
				return Optional.ofNullable(entry.getValue());
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Finds a session by the name of the player
	 *
	 * @param name
	 * 		The name
	 */
	public static Optional<NetworkSession> findByName(String name) {
		for (Player player : World.get().getPlayers()) {
			if (player.getDetails().getUsername().equals(name)) {
				return Optional.ofNullable(player.getNetworkSession());
			}
		}
		for (Player player : World.get().getLobbyPlayers()) {
			if (player.getDetails().getUsername().equals(name)) {
				return Optional.ofNullable(player.getNetworkSession());
			}
		}
		return Optional.empty();
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
	 * Syncs the variables
	 *
	 * @param player
	 * 		The player
	 */
	public void sync(Player player) {
		setPlayer(player);
		player.setNetworkSession(this);
	}
	
	/**
	 * Handles the disconnection of a session
	 */
	void disconnect() {
		if (player != null) {
			if (player.isRenderable()) {
				player.deregister();
			} else {
				player.deregisterLobby();
			}
			player.save();
			MasterCommunication.write(new PlayerDisconnectionPacketOut((byte) GameFlags.worldId, isInLobby(), player.getDetails().getUsername()));
		}
		setPlayer(null);
		UID_MAP.remove(uid);
	}
}

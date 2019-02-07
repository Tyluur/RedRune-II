package org.redrune.networking;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.Setter;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.PacketBuilder;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.session.ISAACCipher;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-02
 */
public class NetworkSession {
	
	/**
	 * The queue of packets that have already been decoded and are awaiting processing
	 */
	private final ConcurrentLinkedQueue<PacketContext> contextQueue = new ConcurrentLinkedQueue<>();
	
	/**
	 * The player affiliated with this network session
	 */
	@Getter
	@Setter
	private Player player;
	
	/**
	 * The channel instance.
	 */
	@Getter
	private Channel channel;
	
	/**
	 * The mac address affiliated with the session
	 */
	@Getter
	@Setter
	private String macAddress;
	
	/**
	 * If the session is in the lobby
	 */
	@Getter
	@Setter
	private boolean inLobby;
	
	/**
	 * The ISAAC cipher for incoming data.
	 */
	@Getter
	@Setter
	private ISAACCipher inCipher;
	
	/**
	 * The ISAAC cipher for outgoing data
	 */
	@Getter
	@Setter
	private ISAACCipher outCipher;
	
	public NetworkSession(Channel channel) {
		this.channel = channel;
	}
	
	/**
	 * This method is invoked when the session is registered
	 */
	public void onRegistration() {
		System.out.println("Session registered! [" + toString() + "]");
	}
	
	@Override
	public String toString() {
		return "NetworkSession{" + "player=" + player + ", inLobby=" + inLobby + '}';
	}
	
	/**
	 * This method is invoked when the session is deregistered
	 */
	public void onDeregistration() {
		if (player != null) {
			if (inLobby) {
				player.finishLobby();
			} else {
				player.finish();
			}
		}
		System.out.println("Session deregistered! [" + toString() + "]");
	}
	
	/**
	 * Writes a packet to the channel and flushes it
	 *
	 * @param bldr
	 * 		The builder of the packet to flush
	 */
	public synchronized ChannelFuture write(OutgoingPacketBuilder bldr) {
		Packet build = bldr.build();
//		System.out.println("Wrote packet " + build);
		return channel.writeAndFlush(build);
	}
	
	/**
	 * Writes a packet to the channel and flushes it
	 *
	 * @param bldr
	 * 		The builder of the packet to flush
	 */
	public synchronized ChannelFuture write(PacketBuilder bldr) {
		Packet msg = bldr.toPacket();
//		System.out.println("Wrote packet " + msg);
		return channel.writeAndFlush(msg);
	}
	
	/**
	 * Gets the ip
	 */
	public String getIPAddress() {
		return Misc.getIpAddress(channel);
	}
	
	/**
	 * Builds the ciphers
	 *
	 * @param inCipher
	 * 		The incoming cipher
	 * @param outCipher
	 * 		The outgoing cipher
	 */
	public void buildCiphers(ISAACCipher inCipher, ISAACCipher outCipher) {
		setInCipher(inCipher);
		setOutCipher(outCipher);
	}
	
	/**
	 * Adds the context of a packet to the queue
	 *
	 * @param context
	 * 		The context
	 */
	public void addContext(PacketContext context) {
		contextQueue.add(context);
	}
	
	/**
	 * Processes the context queue
	 */
	public void processContextQueue() {
		PacketContext context;
		while((context = contextQueue.poll()) != null) {
			context.handle(player);
		}
	}
	
}

package org.redrune.network.master.network;

import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.Setter;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.OutgoingPacket;
import org.redrune.network.master.network.packet.writeable.WriteablePacket;
import org.redrune.network.master.utility.Utility;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class MasterSession {
	
	/**
	 * The channel
	 */
	@Getter
	protected final SocketChannel channel;
	
	/**
	 * The ip of the channel
	 */
	@Getter
	private final String ip;
	
	/**
	 * If the session has been verified
	 */
	@Getter
	@Setter
	protected boolean verified;
	
	/**
	 * Constructs a new network session
	 *
	 * @param channel
	 * 		The channel of the session
	 */
	public MasterSession(SocketChannel channel) {
		this.channel = channel;
		this.ip = Utility.getHost(channel);
		System.out.println("new master session created: " + channel);
	}
	
	@Override
	public String toString() {
		return "LobbySession{" + "channel=" + channel + ", ip='" + ip + '\'' + ", verified=" + verified + '}';
	}
	
	/**
	 * Writes a packet
	 *
	 * @param packet
	 * 		The packet
	 */
	public void write(OutgoingPacket packet) {
		// built in the create method
		if (packet instanceof WriteablePacket) {
			channel.writeAndFlush(((WriteablePacket) packet).create());
		} else {
			channel.writeAndFlush(packet);
		}
//		System.out.println("Writing packet " + packet.getId() + " to master server @ " + MasterConstants.IP);
	}
	
	/**
	 * Handles the reading of a packet
	 *
	 * @param packet
	 * 		The packet
	 */
	public void read(IncomingPacket packet) {
	
	}
	
}

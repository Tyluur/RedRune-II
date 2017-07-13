package master.network;

import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.Setter;
import master.network.packet.IncomingPacket;
import master.network.packet.OutgoingPacket;
import master.network.packet.writeable.WriteablePacket;
import master.utility.Utility;

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
	}
	
	/**
	 * Handles the reading of a packet
	 *
	 * @param packet
	 * 		The packet
	 */
	public void read(IncomingPacket packet) {
	
	}
	
	@Override
	public String toString() {
		return "NetworkSession{" + "ip='" + ip + '\'' + ", verified=" + verified + '}';
	}
}

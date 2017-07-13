package master.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public abstract class Packet {
	
	/**
	 * The id of the packet
	 */
	@Getter
	@Setter
	private int id;
	
	/**
	 * The channel buffer.
	 */
	final ByteBuf buffer;
	
	/**
	 * Constructs a packet with an id
	 *
	 * @param id
	 * 		The id of the packet
	 */
	Packet(int id) {
		this.id = id;
		this.buffer = Unpooled.buffer();
	}
	
	Packet(int id, ByteBuf buffer) {
		this.id = id;
		this.buffer = buffer;
	}
	
	/**
	 * The length of the packet
	 */
	int length() {
		return buffer.writerIndex();
	}
	
}

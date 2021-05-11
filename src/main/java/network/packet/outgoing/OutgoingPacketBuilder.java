package network.packet.outgoing;


import network.packet.Packet;
import network.packet.PacketBuilder;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
public abstract class OutgoingPacketBuilder {
	
	/**
	 * The building of the packet is handled in this method. The {@code PacketBuilder} is converted to a {@code Packet}
	 * via {@link PacketBuilder#toPacket()}
	 *
	 * @return A newly constructed packet
	 */
	public abstract Packet build();
	
	/**
	 * The packet builder instance
	 */

	protected final PacketBuilder bldr;
	
	/**
	 * Constructs the outgoing packet bldr
	 */
	public OutgoingPacketBuilder(PacketBuilder packetBuilder) {
		this.bldr = packetBuilder;
	}
}

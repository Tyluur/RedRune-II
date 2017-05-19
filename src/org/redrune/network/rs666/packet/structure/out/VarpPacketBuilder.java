package org.redrune.network.rs666.packet.structure.out;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public class VarpPacketBuilder implements OutgoingPacketStructure {
	
	/**
	 * The varp id.
	 */
	private final int id;
	
	/**
	 * The value to send.
	 */
	private final int value;
	
	public VarpPacketBuilder(int id, int value) {
		this.id = id;
		this.value = value;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr;
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			bldr = new PacketBuilder(135);
			bldr.writeInt(value);
			bldr.writeLEShort(id);
		} else {
			bldr = new PacketBuilder(123);
			bldr.writeByte(value);
			bldr.writeShort(id);
		}
		return bldr.toPacket();
	}
}

package org.redrune.network.rs666.packet.structure.out;

import org.redrune.anetworking.rs666.packet.PacketBuilder;
import org.redrune.anetworking.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class AccessMaskBuilder implements OutgoingPacketStructure {
	
	/**
	 * The interface id.
	 */
	private final int interfaceId;
	
	/**
	 * The secondary child id
	 */
	private final int interfaceId2;
	
	/**
	 * The child id.
	 */
	private final int childId;
	
	/**
	 * The secondary child id
	 */
	private final int childId2;
	
	/**
	 * The minimum slot.
	 */
	private final int min;
	
	/**
	 * The maximum slot.
	 */
	private final int max;
	
	public AccessMaskBuilder(int interfaceId, int interfaceId2, int childId, int childId2, int min, int max) {
		this.interfaceId = interfaceId;
		this.interfaceId2 = interfaceId2;
		this.childId = childId;
		this.childId2 = childId2;
		this.min = min;
		this.max = max;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(42);
		bldr.writeLEShort(min);
		bldr.writeLEInt(interfaceId2 << 16 | childId2); //Value
		bldr.writeInt1(interfaceId << 16 | childId);
		bldr.writeLEShort(max);
		return bldr.toPacket();
	}
}

package org.redrune.network.rs666.packet.structure.out;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
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
	
	/**
	 * Constructs a new {@code AccessMaskBuilder} {@code Object}.
	 *
	 * @param interfaceId
	 * 		The interface id.
	 * @param childId
	 * 		The child id.
	 * @param min
	 * 		The minimum slot.
	 * @param max
	 * 		The maximum slot
	 * @param childId2
	 * 		The second child id
	 * @param interfaceId2
	 * 		The second interface id
	 */
	public AccessMaskBuilder(int interfaceId, int childId, int interfaceId2, int childId2, int min, int max) {
		this.min = min;
		this.max = max;
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.interfaceId2 = interfaceId2;
		this.childId2 = childId2;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(42);
		bldr.writeLEShort(min);
		bldr.writeLEInt(interfaceId2 << 16 | childId2);
		bldr.writeInt1(interfaceId << 16 | childId);
		bldr.writeLEShort(max);
		return bldr.toPacket();
	}
}
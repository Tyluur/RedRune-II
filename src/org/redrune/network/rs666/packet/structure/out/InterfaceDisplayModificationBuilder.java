package org.redrune.network.rs666.packet.structure.out;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class InterfaceDisplayModificationBuilder implements OutgoingPacketStructure {
	
	/**
	 * The interface id.
	 */
	private final int interfaceId;
	
	/**
	 * The child id.
	 */
	private final int childId;
	
	/**
	 * If we should hide the child.
	 */
	private final boolean hide;
	
	/**
	 * Constructs a new {@code InterfaceDisplayModificationBuilder} {@code Object}
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 * @param childId
	 * 		The child id of the interface
	 * @param hide
	 * 		If we should hide the child
	 */
	public InterfaceDisplayModificationBuilder(int interfaceId, int childId, boolean hide) {
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.hide = hide;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(102);
		bldr.writeByte(hide ? 1 : 0);
		bldr.writeInt(interfaceId << 16 | childId);
		return bldr.toPacket();
	}
}

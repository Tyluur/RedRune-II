package org.redrune.network.rs666.packet.structure.out;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public class InterfaceDisplayBuilder implements OutgoingPacketStructure {
	
	/**
	 * The window id.
	 */
	private final int windowId;
	
	/**
	 * The interface id.
	 */
	private final int interfaceId;
	
	/**
	 * The child id.
	 */
	private final int childId;
	
	/**
	 * If the interface is an overlay.
	 */
	private boolean walkable;
	
	public InterfaceDisplayBuilder(int windowId, int interfaceId, int childId, boolean walkable) {
		this.windowId = windowId;
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.walkable = walkable;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(139);
		bldr.writeByteS(walkable ? 1 : 0);
		bldr.writeShortA(childId);
		bldr.writeInt(windowId << 16 | interfaceId);
		return bldr.toPacket();
	}
}

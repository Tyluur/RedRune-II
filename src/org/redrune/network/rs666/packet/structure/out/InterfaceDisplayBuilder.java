package org.redrune.network.rs666.packet.structure.out;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class InterfaceDisplayBuilder implements OutgoingPacketStructure {
	
	/**
	 * The window id.
	 */
	private final int windowId;
	
	/**
	 * The interface id.
	 */
	private final int childId;
	
	/**
	 * The child id.
	 */
	private final int interfaceId;
	
	/**
	 * If the interface is an overlay.
	 */
	private boolean transparent;
	
	/**
	 * Constructs a new interface display builder
	 *
	 * @param windowId
	 * 		The window id of the interface
	 * @param childId
	 * 		The child id of the interface (where to display it)
	 * @param interfaceId
	 * 		The id of the interface
	 * @param transparent
	 * 		If we should display the interface as transparent
	 */
	public InterfaceDisplayBuilder(int windowId, int childId, int interfaceId, boolean transparent) {
		this.windowId = windowId;
		this.childId = childId;
		this.interfaceId = interfaceId;
		this.transparent = transparent;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(139);
		bldr.writeByteS(transparent ? 1 : 0);
		bldr.writeShortA(interfaceId);
		bldr.writeInt(windowId << 16 | childId);
		return bldr.toPacket();
	}
}

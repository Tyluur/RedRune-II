package org.redrune.network.rs666.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.outgoing.OutgoingPacketStructure;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class InterfaceDisplayBuilder implements OutgoingPacketStructure {
	
	/**
	 * The window id.
	 */
	private final int paneId;
	
	/**
	 * The interface id.
	 */
	private final int componentId;
	
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
	 * @param paneId
	 * 		The pane id of the interface
	 * @param componentId
	 * 		The component id of the interface (where to display it)
	 * @param interfaceId
	 * 		The id of the interface
	 * @param transparent
	 * 		If we should display the interface as transparent
	 */
	public InterfaceDisplayBuilder(int paneId, int componentId, int interfaceId, boolean transparent) {
		this.paneId = paneId;
		this.componentId = componentId;
		this.interfaceId = interfaceId;
		this.transparent = transparent;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(139);
		bldr.writeByteS(transparent ? 1 : 0);
		bldr.writeShortA(interfaceId);
		bldr.writeInt(paneId << 16 | componentId);
		return bldr.toPacket();
	}
}

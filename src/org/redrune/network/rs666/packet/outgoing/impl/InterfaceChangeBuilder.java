package org.redrune.network.rs666.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.outgoing.OutgoingPacketStructure;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class InterfaceChangeBuilder implements OutgoingPacketStructure {
	
	/**
	 * The interface id.
	 */
	private final int interfaceId;
	
	/**
	 * The child id.
	 */
	private final int componentId;
	
	/**
	 * If we should hide the child.
	 */
	private final boolean hide;
	
	/**
	 * Constructs a new {@code InterfaceDisplayModificationBuilder} {@code Object}
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 * @param componentId
	 * 		The child id of the interface
	 * @param hide
	 * 		If we should hide the child
	 */
	public InterfaceChangeBuilder(int interfaceId, int componentId, boolean hide) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.hide = hide;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(102);
		bldr.writeByte(hide ? 1 : 0);
		bldr.writeInt(interfaceId << 16 | componentId);
		return bldr.toPacket();
	}
}

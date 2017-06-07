package org.redrune.network.rs666.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class InterfaceEntityBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The interface to show the entity on
	 */
	private final int interfaceId;
	
	/**
	 * The component of the interface to show the entity on
	 */
	private final int componentId;
	
	/**
	 * The id of the npc to draw
	 */
	private final int npcId;
	
	public InterfaceEntityBuilder(int interfaceId, int componentId) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.npcId = -1;
	}
	
	public InterfaceEntityBuilder(int interfaceId, int componentId, int npcId) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.npcId = npcId;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr;
		if (npcId == -1) {
			bldr = new PacketBuilder(48);
			bldr.writeInt1(interfaceId << 16 | componentId);
		} else {
			bldr = new PacketBuilder(136);
			bldr.writeInt(interfaceId << 16 | componentId);
			bldr.writeLEShortA(npcId);
		}
		return bldr.toPacket();
	}
}

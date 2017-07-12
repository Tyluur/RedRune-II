package org.redrune.network.rs666.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/26/2017
 */
public class WeightBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The weight to send
	 */
	private final double weight;
	
	/**
	 * The weight packet builder
	 *
	 * @param weight
	 * 		The weight to send
	 */
	public WeightBuilder(double weight) {
		this.weight = weight;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(0);
		bldr.writeShort((int) weight);
		return bldr.toPacket();
	}
}

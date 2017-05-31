package org.redrune.network.rs666.packet.structure.out;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class PingPacketBuilder implements OutgoingPacketStructure {
	
	@Override
	public Packet build(Player player) {
		return new PacketBuilder(128).toPacket();
	}
}

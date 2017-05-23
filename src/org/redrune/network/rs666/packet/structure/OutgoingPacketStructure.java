package org.redrune.network.rs666.packet.structure;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.rs2.node.entity.player.Player;

/**
 * The structure of an outgoing packet.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public interface OutgoingPacketStructure {
	
	/**
	 * The building of the packet is handled in this method. The {@code PacketBuilder} is converted to a {@code Packet}
	 * via {@link PacketBuilder#toPacket()}
	 *
	 * @return A newly constructed packe
	 */
	Packet build(Player player);
	
}

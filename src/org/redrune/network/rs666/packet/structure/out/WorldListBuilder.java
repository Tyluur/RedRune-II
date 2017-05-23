package org.redrune.network.rs666.packet.structure.out;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.world.connection.WorldList;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class WorldListBuilder implements OutgoingPacketStructure {
	
	@Override
	public Packet build(Player player) {
		return WorldList.getData(true, true).toPacket();
	}
}

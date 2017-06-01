package org.redrune.network.rs666.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.list.WorldList;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.outgoing.OutgoingPacketStructure;

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

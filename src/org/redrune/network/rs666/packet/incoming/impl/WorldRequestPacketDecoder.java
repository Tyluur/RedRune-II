package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;
import org.redrune.network.rs666.packet.outgoing.impl.WorldListBuilder;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class WorldRequestPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(33);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		if (player.getNetworkSession().isInLobby()) {
			player.getTransmitter().send(new WorldListBuilder().build(player));
		}
	}
}

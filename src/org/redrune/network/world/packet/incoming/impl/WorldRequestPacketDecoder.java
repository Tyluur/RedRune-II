package org.redrune.network.world.packet.incoming.impl;

import org.redrune.utility.tool.Misc;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.network.world.packet.outgoing.impl.WorldListBuilder;

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
	/*	long serverKey = packet.readLong();
		boolean containsInformation = packet.readByte() == 1;
		boolean containsStatus = packet.readByte() == 1;*/
		if (player.getSession().isInLobby()) {
			player.getTransmitter().send(new WorldListBuilder(true, true).build(player));
		}
	}
}

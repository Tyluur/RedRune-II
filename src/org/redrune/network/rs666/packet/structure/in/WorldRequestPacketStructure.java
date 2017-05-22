package org.redrune.network.rs666.packet.structure.in;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.network.rs666.packet.structure.out.WorldListBuilder;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class WorldRequestPacketStructure implements IncomingPacketStructure {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(33);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		long serverKey = packet.readLong();
		boolean containsInformation = packet.readByte() == 1;
		boolean containsStatus = packet.readByte() == 1;
		System.out.println(serverKey + ", " + containsInformation + ", " + containsStatus);
		if (player.getNetworkSession().isInLobby()) {
			player.getTransmitter().send(new WorldListBuilder().build(player));
		}
	}
}

package org.redrune.network.rs666.packet.structure.in;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class InterfaceClosedPacketStructure implements IncomingPacketStructure {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(64);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		player.getManager().getInterfaces().closeAllInterfaces();
	}
}

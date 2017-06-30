package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class InterfaceClosedPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(64);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		player.getManager().getInterfaces().closeAllInterfaces();
	}
}

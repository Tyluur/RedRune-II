package org.redrune.network.rs666.packet.structure.in;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.context.WalkEventContext;
import org.redrune.game.node.entity.player.event.impl.WalkEvent;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class WalkPacketStructure implements IncomingPacketStructure {
	
	/**
	 * The important opcodes
	 */
	private static final int MINIMAP_CLICK = 53, GAME_CLICK = 56;
	
	@Override
	public int[] bindings() {
		return Misc.arguments(GAME_CLICK, MINIMAP_CLICK);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		int steps = (packet.getLength() - 5) >> 1;
		if (steps > 25) {
			return;
		}
		int y = packet.readLEShort();
		boolean running = packet.readByteC() == 1;
		int x = packet.readLEShortA();
		player.getManager().getEvents().executeEvent(player, new WalkEvent(new WalkEventContext(x, y, running)));
	}
}

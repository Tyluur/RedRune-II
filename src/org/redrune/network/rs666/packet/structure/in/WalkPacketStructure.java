package org.redrune.network.rs666.packet.structure.in;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.world.map.path.PathFactory;
import org.redrune.rs2.world.map.path.finder.DefaultPathFinder;
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
		player.getWalkingQueue().reset(running);
		PathFactory.get().doPath(new DefaultPathFinder(), player, x, y);
	}
}

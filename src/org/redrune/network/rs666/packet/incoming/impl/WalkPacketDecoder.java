package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.context.WalkEventContext;
import org.redrune.game.node.entity.player.event.impl.WalkEvent;
import org.redrune.game.node.entity.player.link.EventManager;
import org.redrune.game.world.route.RouteFinder;
import org.redrune.game.world.route.strategy.FixedTileStrategy;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class WalkPacketDecoder implements IncomingPacketDecoder {
	
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
		
		// finished reading
		
		int calculatedSteps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getPlane(), player.getSize(), new FixedTileStrategy(x, y), true);
		int[] bufferX = RouteFinder.getLastPathBufferX();
		int[] bufferY = RouteFinder.getLastPathBufferY();
		
		// if we're walking, we wait for it to stop bc render emote must happen first.
		if (player.getAttribute("walking", false)) {
			return;
		}
		
		// execute the event now.
		EventManager.executeEvent(player, WalkEvent.class, new WalkEventContext(x, y, bufferX, bufferY, running, calculatedSteps));
	}
}

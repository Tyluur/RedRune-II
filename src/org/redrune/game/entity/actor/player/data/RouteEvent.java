package org.redrune.game.entity.actor.player.data;

import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.FloorItem;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.map.route.RouteFinder;
import org.redrune.game.global.map.route.RouteStrategy;
import org.redrune.game.global.map.route.strategy.ActorStrategy;
import org.redrune.game.global.map.route.strategy.FixedTileStrategy;
import org.redrune.game.global.map.route.strategy.FloorItemStrategy;
import org.redrune.game.global.map.route.strategy.ObjectStrategy;

public class RouteEvent {
	
	/**
	 * Object to which we are finding the route.
	 */
	private Object destination;
	
	/**
	 * The event instance.
	 */
	private Runnable event;
	
	/**
	 * Whether we also run on alternative.
	 */
	private boolean alternative;
	
	/**
	 * Contains last route strategies.
	 */
	private RouteStrategy[] last;
	
	/**
	 * If we've faced the destination, only applicable in actor cases
	 */
	private boolean facedDestination;
	
	public RouteEvent(Object destination, Runnable event) {
		this(destination, event, false);
	}
	
	public RouteEvent(Object destination, Runnable event, boolean alternative) {
		this.destination = destination;
		this.event = event;
		this.alternative = alternative;
	}
	
	public boolean processEvent(final Player player) {
		if (!simpleCheck(player)) {
			player.getPackets().sendGameMessage("You can't reach that.");
			player.getPackets().sendResetMinimapFlag();
			return true;
		}
		if (player.getLocks().isMovementLocked()) {
			return true;
		}
		RouteStrategy[] strategies = generateStrategies();
		// so we face actors when we're pathing to them
		if (!facedDestination && destination instanceof Actor) {
			player.setNextFaceActor((Actor) destination);
			facedDestination = true;
		}
		if (last != null && match(strategies, last) && player.hasWalkSteps()) {
			return false;
		} else if (last != null && match(strategies, last) && !player.hasWalkSteps()) {
			for (int i = 0; i < strategies.length; i++) {
				RouteStrategy strategy = strategies[i];
				int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getPlane(), player.getSize(), strategy, i == (strategies.length - 1));
				if (steps == -1) {
					continue;
				}
				if ((!RouteFinder.lastIsAlternative() && steps <= 0) || alternative) {
					if (alternative) {
						player.getPackets().sendResetMinimapFlag();
					}
					return runEvent(player);
				}
			}
			
			player.getPackets().sendGameMessage("You can't reach that.");
			player.getPackets().sendResetMinimapFlag();
			return true;
		} else {
			last = strategies;
			
			for (int i = 0; i < strategies.length; i++) {
				RouteStrategy strategy = strategies[i];
				int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getPlane(), player.getSize(), strategy, i == (strategies.length - 1));
				if (steps == -1) {
					continue;
				}
				if ((!RouteFinder.lastIsAlternative() && steps <= 0)) {
					if (alternative) {
						player.getPackets().sendResetMinimapFlag();
					}
					return runEvent(player);
				}
				int[] bufferX = RouteFinder.getLastPathBufferX();
				int[] bufferY = RouteFinder.getLastPathBufferY();
				
				WorldTile last = new WorldTile(bufferX[0], bufferY[0], player.getPlane());
				player.resetWalkSteps();
				player.getPackets().sendMinimapFlag(last.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()), last.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
				if (player.isFrozen()) {
					return false;
				}
				for (int step = steps - 1; step >= 0; step--) {
					if (player.addWalkSteps(bufferX[step], bufferY[step], 25, true)) {
						continue;
					}
					break;
				}
				return false;
			}
			
			player.getPackets().sendGameMessage("You can't reach that.");
			player.getPackets().sendResetMinimapFlag();
			return true;
		}
	}
	
	private boolean simpleCheck(Player player) {
		if (destination instanceof Actor) {
			return player.getPlane() == ((Actor) destination).getPlane();
		} else if (destination instanceof WorldObject) {
			return player.getPlane() == ((WorldObject) destination).getPlane();
		} else if (destination instanceof FloorItem) {
			return player.getPlane() == ((FloorItem) destination).getTile().getPlane();
		} else {
			throw new RuntimeException(destination + " is not instanceof any reachable actor.");
		}
	}
	
	private RouteStrategy[] generateStrategies() {
		if (destination instanceof Actor) {
			return new RouteStrategy[] { new ActorStrategy((Actor) destination) };
		} else if (destination instanceof WorldObject) {
			return new RouteStrategy[] { new ObjectStrategy((WorldObject) destination) };
		} else if (destination instanceof FloorItem) {
			FloorItem item = (FloorItem) destination;
			return new RouteStrategy[] { new FixedTileStrategy(item.getTile().getX(), item.getTile().getY()), new FloorItemStrategy(item) };
		} else {
			throw new RuntimeException(destination + " is not instanceof any reachable actor.");
		}
	}
	
	private boolean match(RouteStrategy[] a1, RouteStrategy[] a2) {
		if (a1.length != a2.length) {
			return false;
		}
		for (int i = 0; i < a1.length; i++) {
			if (!a1[i].equals(a2[i])) {
				return false;
			}
		}
		return true;
	}
	
	private boolean runEvent(Player player) {
		boolean moving = player.hasWalkSteps() || player.getNextRunDirection() != -1 || player.getNextWalkDirection() != -1;
		if (moving) {
			return false;
		}
		event.run();
		return true;
	}
	
}

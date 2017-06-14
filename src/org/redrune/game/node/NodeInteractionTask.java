package org.redrune.game.node;

import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.FloorItem;
import org.redrune.game.world.route.RouteFinder;
import org.redrune.game.world.route.RouteStrategy;
import org.redrune.game.world.route.strategy.EntityStrategy;
import org.redrune.game.world.route.strategy.FixedTileStrategy;
import org.redrune.game.world.route.strategy.FloorItemStrategy;
import org.redrune.game.world.route.strategy.ObjectStrategy;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class NodeInteractionTask {
	
	/**
	 * The node we will be travelling to
	 */
	private final Node node;
	
	/**
	 * The task to execute once we arrive at the node's closest location.
	 */
	private final Runnable task;
	
	/**
	 * Whether we also run on alternative.
	 */
	private final boolean alternative;
	
	/**
	 * Contains last route strategies.
	 */
	private RouteStrategy[] last;
	
	/**
	 * Constructs a new path event
	 *
	 * @param node
	 * 		The node
	 * @param task
	 * 		The task to execute
	 * @param alternative
	 * 		If we should run alternative
	 */
	public NodeInteractionTask(Node node, Runnable task, boolean alternative) {
		this.node = node;
		this.task = task;
		this.alternative = alternative;
	}
	
	/**
	 * Processes the event, if it is over, we will return true.
	 *
	 * @param player
	 * 		The player whose path event this is
	 */
	public boolean process(Player player) {
		if (!simpleCheck(player)) {
			player.getTransmitter().sendMessage("You can't reach that.");
			player.getTransmitter().sendMinimapFlagReset();
			return true;
		}
		
		if (node.isNPC()) {
			player.turnTo(node.toNPC());
		}
		
		RouteStrategy[] strategies = generateStrategies();
		if (last != null && match(strategies, last) && player.getMovement().isMoving()) {
			return false;
		} else if (last != null && match(strategies, last) && !player.getMovement().isMoving()) {
			for (int i = 0; i < strategies.length; i++) {
				RouteStrategy strategy = strategies[i];
				int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getPlane(), player.getSize(), strategy, i == (strategies.length - 1));
				if (steps == -1) {
					continue;
				}
				if ((!RouteFinder.lastIsAlternative() && steps <= 0) || alternative) {
					if (alternative) {
						player.getTransmitter().sendMinimapFlagReset();
					}
					if (!player.getMovement().isMoving()) {
						executeTask(player);
					}
					return true;
				}
			}
			
			player.getTransmitter().sendMessage("You can't reach that.");
			player.getTransmitter().sendMinimapFlagReset();
			return true;
		} else {
			last = strategies;
			
			for (int i = 0; i < strategies.length; i++) {
				RouteStrategy strategy = strategies[i];
				int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getPlane(), player.getSize(), strategy, i == (strategies.length - 1));
				if (steps == -1) {
					continue;
				}
				if ((!RouteFinder.lastIsAlternative() && steps <= 0)) {
					if (alternative) {
						player.getTransmitter().sendMinimapFlagReset();
					}
					if (!player.getMovement().isMoving()) {
						executeTask(player);
					}
					return true;
				}
				int[] bufferX = RouteFinder.getLastPathBufferX();
				int[] bufferY = RouteFinder.getLastPathBufferY();
				
				Location last = new Location(bufferX[0], bufferY[0], player.getLocation().getPlane());
				player.getMovement().resetWalkSteps();
				player.getTransmitter().sendMinimapFlag(last.getLocalX(player.getLastLoadedLocation()), last.getLocalY(player.getLastLoadedLocation()));
				for (int step = steps - 1; step >= 0; step--) {
					if (!player.getMovement().addWalkSteps(bufferX[step], bufferY[step], 25, true)) {
						break;
					}
				}
				
				return false;
			}
			
			player.getTransmitter().sendMessage("You can't reach that.");
			player.getTransmitter().sendMinimapFlagReset();
			return true;
		}
	}
	
	/**
	 * Executes the task
	 *
	 * @param player
	 * 		The player executing it
	 */
	private void executeTask(Player player) {
		task.run();
	}
	
	/**
	 * Checks that the node is a node we can travel to with a strategy, and then that we are on the same tile as them
	 *
	 * @param player
	 * 		The player travelling
	 */
	private boolean simpleCheck(Player player) {
		if (!node.isPlayer() && !node.isNPC() && !node.isGameObject() && !node.isItem()) {
			throw new RuntimeException(node + " is not instanceof any reachable entity.");
		}
		return player.getLocation().getPlane() == node.getLocation().getPlane();
	}
	
	/**
	 * Generates strategies to travel to the node
	 */
	private RouteStrategy[] generateStrategies() {
		if (node.isPlayer() || node.isNPC()) {
			return new RouteStrategy[] { new EntityStrategy((Entity) node) };
		} else if (node.isGameObject()) {
			return new RouteStrategy[] { new ObjectStrategy(node.toGameObject()) };
		} else if (node.isItem()) {
			FloorItem item = (FloorItem) node.toItem();
			return new RouteStrategy[] { new FixedTileStrategy(item.getLocation().getX(), item.getLocation().getY()), new FloorItemStrategy(item) };
		} else {
			throw new RuntimeException(node + " is not instanceof any reachable entity.");
		}
	}
	
	/**
	 * Checks that the two strategies match
	 *
	 * @param strategies
	 * 		The strategy
	 * @param routeStrategies
	 * 		The second strategy
	 */
	private boolean match(RouteStrategy[] strategies, RouteStrategy[] routeStrategies) {
		if (strategies.length != routeStrategies.length) {
			return false;
		}
		for (int i = 0; i < strategies.length; i++) {
			if (!strategies[i].equals(routeStrategies[i])) {
				return false;
			}
		}
		return true;
	}
	
}
package org.redrune.game.world.path;

import org.redrune.game.node.Location;
import org.redrune.game.node.Node;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.world.path.finder.DefaultPathFinder;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.Misc;

import java.util.*;

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
	 * The tile we will be travelling to. This is not the same as the {@link #node}'s location because we will
	 * travel to the best location for the node interaction.
	 */
	private Location targetTile;
	
	/**
	 * The path state
	 */
	private PathState state = null;
	
	/**
	 * Constructs a new path event
	 *
	 * @param node
	 * 		The node
	 * @param task
	 * 		The task to execute
	 */
	public NodeInteractionTask(Node node, Runnable task) {
		this.node = node;
		this.task = task;
	}
	
	/**
	 * Processes the event, if it is over, we will return true.
	 *
	 * @param player
	 * 		The player whose path event this is
	 */
	public boolean process(Player player) {
		final boolean moving = player.getWalkingQueue().isMoving();
		if (state == null) {
			targetTile = generateTargetTile(Location.create(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getPlane()));
			
			// we're already at the location
			if (targetTile != null && arrived(player) && !moving) {
				task.run();
				return true;
			}
			
			state = PathFactory.get().doPath(new DefaultPathFinder(), player, targetTile.getX(), targetTile.getY());
			return false;
		} else {
			if (moving && node.isNPC()) {
				player.turnTo(node.toNPC());
			}
			if (!state.isRouteFound() && !moving) {
				player.getTransmitter().sendMessage("You couldn't reach that.");
				return true;
			}
			if (arrived(player)) {
				if (moving) {
					return false;
				}
				task.run();
				return true;
			} else {
				return !moving;
			}
		}
	}
	
	/**
	 * Generates the target tile
	 *
	 * @param startLocation
	 * 		The start location for this task
	 */
	private Location generateTargetTile(Location startLocation) {
		if (node instanceof NPC || node instanceof GameObject) {
			final boolean isBanker = node.isNPC() && node.toNPC().getDefinitions().getName().toLowerCase().contains("banker");
			int size = node.isNPC() && isBanker ? 2 : node.getSize();
			
			// adds the tiles around the npc
			List<Location> surroundingTiles = new ArrayList<>();
			for (int diffX = -size; diffX <= size; diffX++) {
				if (diffX == 0) {
					continue;
				}
				
				final Location location = node.getLocation().transform(diffX, 0, 0);
				if (!surroundingTiles.contains(location)) {
					surroundingTiles.add(location);
				}
			}
			for (int diffY = -size; diffY <= size; diffY++) {
				if (diffY == 0) {
					continue;
				}
				final Location location = node.getLocation().transform(0, diffY, 0);
				if (!surroundingTiles.contains(location)) {
					surroundingTiles.add(location);
				}
			}
			
			// removes tiles the player cant access
			for (Iterator<Location> iterator = surroundingTiles.iterator(); iterator.hasNext(); ) {
				Location loc = iterator.next();
				if (loc.equals(startLocation)) {
					return loc;
				}
				// the direction, used to find the mask based on the location
				final int xOffset = startLocation.getX() - loc.getX();
				final int yOffset = startLocation.getY() - loc.getY();
				int dir = Misc.getMoveDirection(xOffset, yOffset);
				if (dir == -1) {
					//					System.out.println("REMOVED LOCATION[1]: " + loc + "\t,dir=" + dir + ",xOffset=" + xOffset + ",yOffset=" + yOffset);
					iterator.remove();
					continue;
				}
				if (!RegionManager.isTileFree(loc.getPlane(), loc.getX(), loc.getY(), dir, 1)) {
					//					System.out.println("REMOVED LOCATION[2]: " + loc + "\t,dir=" + dir + ",xOffset=" + xOffset + ",yOffset=" + yOffset);
					iterator.remove();
				}
			}
			
			// sorts the tiles based on distance.
			surroundingTiles.sort(Comparator.comparingInt(o -> o.getDistance(startLocation)));
			
			// TODO fuck this bullshit we need a prooper pf system
			// make sure we're not interaction thru a wall
			if (!isBanker && !node.isGameObject()) {
				for (Iterator<Location> iterator = surroundingTiles.iterator(); iterator.hasNext(); ) {
					Location loc = iterator.next();
					
					// the direction, used to find the mask based on the location
					final int xOffset = node.getLocation().getX() - loc.getX();
					final int yOffset = node.getLocation().getY() - loc.getY();
					int dir = Misc.getMoveDirection(xOffset, yOffset);
					if (dir == -1) {
						//					System.out.println("REMOVED LOCATION[3]: " + loc + "\t,dir=" + dir + ",xOffset=" + xOffset + ",yOffset=" + yOffset);
						iterator.remove();
						continue;
					}
					if (!RegionManager.isTileFree(loc.getPlane(), loc.getX(), loc.getY(), dir, 1)) {
						//					System.out.println("REMOVED LOCATION[4]: " + loc + "\t,dir=" + dir + ",xOffset=" + xOffset + ",yOffset=" + yOffset);
						iterator.remove();
					}
				}
			}
		/*
			for (Location loc : surroundingTiles) {
				RegionManager.addFloorItem(14484, 1, 10, loc, null);
			}
			*/
			if (surroundingTiles.isEmpty()) {
				surroundingTiles.add(node.getLocation());
			}
			
	/*		List<NPC> npcs = new ArrayList<>();
			for (Location loc : surroundingTiles) {
				System.out.println(loc);
				npcs.add(World.get().addNPC(1, loc, Direction.NORTH));
			}
			
			SystemManager.getScheduler().schedule(new ScheduledTask(5, 1, false, () -> {
				for (NPC npc : npcs) {
					npc.deregister();
				}
			}));*/
			
			//			System.out.println("target tile: " + surroundingTiles.get(0));
/*			if (surroundingTiles.size() > 1) {
				System.out.println("others: " + surroundingTiles.subList(1, surroundingTiles.size()));
			}*/
			return surroundingTiles.get(0);
		} else {
			return node.getLocation();
		}
	}
	
	/**
	 * Checks if the player arrived at the destination
	 *
	 * @param player
	 * 		The player
	 */
	private boolean arrived(Player player) {
		if (node.isItem()) {
			return Objects.equals(player.getLocation(), targetTile);
		} else if (node.isNPC()) {
			if (node.toNPC().getDefinitions().getName().toLowerCase().contains("banker")) {
				return targetTile.getDistance(player.getLocation()) <= 2;
			}
			return targetTile.equals(player.getLocation());
		}
		return Objects.equals(player.getLocation(), targetTile);
	}
	
}
package org.redrune.rs2.node.entity.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.rs2.node.entity.Entity;
import org.redrune.rs2.node.entity.player.render.flag.impl.TeleportUpdate;
import org.redrune.rs2.world.map.Location;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.Misc;
import org.redrune.rs2.world.map.Directions;
import org.redrune.rs2.world.map.Directions.WalkingDirection;

import java.util.Deque;
import java.util.LinkedList;

/**
 * The entity's walking queue.
 *
 * @author Emperor
 * @author Graham
 * @author Mystic flow'
 */
public class WalkingQueue {
	
	/**
	 * The walking queue.
	 */
	private final Deque<Point> walkingQueue = new LinkedList<>();
	
	/**
	 * The entity.
	 */
	private final Entity entity;
	
	/**
	 * The current walking direction.
	 */
	@Getter
	@Setter
	private int walkDir = -1;
	
	/**
	 * The current running direction.
	 */
	@Getter
	@Setter
	private int runDir = -1;
	
	/**
	 * If the entity is running (set to true when holding the ctrl button +
	 * click).
	 */
	@Setter
	private boolean running = false;
	
	/**
	 * The last location this entity walked on.
	 */
	@Getter
	@Setter
	private Location footPrint;
	
	/**
	 * Constructs a new {@code WalkingQueue} {@code Object}.
	 *
	 * @param entity
	 * 		The entity.
	 */
	public WalkingQueue(Entity entity) {
		this.entity = entity;
		this.footPrint = entity.getLocation();
	}
	
	/**
	 * Updates the walking queue.
	 */
	public void updateMovement() {
		boolean isPlayer = entity.isPlayer();
		if (updateTeleport()) {
			return;
		}
		this.walkDir = -1;
		this.runDir = -1;
		Point walkPoint = walkingQueue.poll();
		Point runPoint = null;
		if (walkPoint == null) {
			/*if (entity.isPlayer()) {
				entity.toPlayer().getSettings().increaseRunEnergy(0.264);
			}*/
			return;
		}
		if (walkPoint.direction == null) {
			walkPoint = walkingQueue.poll();
		}
		int walkDirection = -1;
		int runDirection = -1;
		if (isRunning()) {
			runPoint = walkingQueue.poll();
		}
		if (walkPoint != null) {
			walkDirection = isPlayer ? walkPoint.direction.intValue() : walkPoint.direction.npcIntValue();
		}
		if (runPoint != null) {
			runDirection = isPlayer ? runPoint.direction.intValue() : runPoint.direction.npcIntValue();
		}
		if (isPlayer && updateRegion(walkPoint, runPoint)) {
			return;
		}
		int diffX = 0;
		int diffY = 0;
		if (walkDirection != -1) {
			diffX += Location.DIRECTION_DELTA_X[walkPoint.direction.intValue()];
			diffY += Location.DIRECTION_DELTA_Y[walkPoint.direction.intValue()];
		}
		if (runDirection != -1) {
			int nextXDiff = Location.DIRECTION_DELTA_X[runDirection];
			int nextYDiff = Location.DIRECTION_DELTA_Y[runDirection];
			if (isPlayer) {
				runDirection = Misc.getRunningDirection(diffX + nextXDiff, diffY + nextYDiff);
			}
			if (runDirection != -1) {
				walkDirection = -1;
				diffX += nextXDiff;
				diffY += nextYDiff;
			} else if (walkDirection == -1) {
				walkDirection = (byte) Misc.getWalkDirection(nextXDiff, nextYDiff);
				diffX += nextXDiff;
				diffY += nextYDiff;
			}
			if (entity.isPlayer()) {
				// entity.toPlayer().getSettings().decreaseRunEnergy(1.5);
				// entity.toPlayer().getSettings().decreaseRunEnergy(getEnergyDrainRate(entity.toPlayer())
				// <= 0 ? 1.5 : getEnergyDrainRate(entity.toPlayer()));
			}
		} else if (entity.isPlayer()) {
			//	entity.toPlayer().getSettings().increaseRunEnergy(0.264);
		}
		if (diffX != 0 || diffY != 0) {
			footPrint = entity.getLocation();
			entity.setLocation(entity.getLocation().transform(diffX, diffY, 0));
			//Main.getWorkingSet().submitLogic(new AreaUpdateTick(entity));
		}
		this.walkDir = walkDirection;
		this.runDir = runDirection;
	}
	
	/**
	 * Checks if the player is teleporting, if so does the teleporting and
	 * returns true.
	 *
	 * @return {@code True} if the player is teleporting, {@code false} if not.
	 */
	private boolean updateTeleport() {
		if (entity.getAttribute(AttributeKey.TELEPORT_LOCATION) != null) {
			reset(false);
			Location lastRegion = entity.getLocation();
			footPrint = entity.getAttribute(AttributeKey.TELEPORT_LOCATION);
			entity.setLocation(entity.getAttribute(AttributeKey.TELEPORT_LOCATION));
			entity.removeAttribute(AttributeKey.TELEPORT_LOCATION);
			entity.getUpdateMasks().register(new TeleportUpdate());
			if ((lastRegion.getRegionX() - entity.getLocation().getRegionX()) >= 4 || (lastRegion.getRegionX() - entity.getLocation().getRegionX()) <= -4) {
				entity.putAttribute(AttributeKey.MAP_REGION_CHANGED, true);
			}
			if ((lastRegion.getRegionY() - entity.getLocation().getRegionY()) >= 4 || (lastRegion.getRegionY() - entity.getLocation().getRegionY()) <= -4) {
				entity.putAttribute(AttributeKey.MAP_REGION_CHANGED, true);
			}
			entity.putAttribute(AttributeKey.PLAYER_TELEPORTED, true);
			return true;
		}
		return false;
	}

	/*
	 * public double getEnergyDrainRate(Player player) { return
	 * player.getProperties().getCarriedWeight(player) == 0 ? 0 : (int)
	 * Math.ceil(7.6 - ((player.getSkills() .getLevel(Skills.AGILITY) / 99D) *
	 * (int) player.getProperties() .getCarriedWeight(player))); }
	 */
	
	/**
	 * Checks if the entity is running.
	 *
	 * @return {@code True} if a ctrl + click action was performed, <br> the player has the run option enabled or the
	 * NPC is a familiar, <p> {@code false} if not.
	 */
	public boolean isRunning() {
		return running || (entity.isPlayer() && entity.toPlayer().getVariables().isRunToggled()) || (entity.isNPC());
	}
	
	/**
	 * Checks if the region should be updated, if so we set the update flag and
	 * return true.
	 *
	 * @param runPoint
	 * 		a run point
	 * @param walkPoint
	 * 		a walk point (y)(y)(y)
	 * @return {@code True} if the region updated, {@code false} if not.
	 */
	private boolean updateRegion(Point walkPoint, Point runPoint) {
		Location lastRegion = entity.toPlayer().getDetails().getLastLocation() != null ? entity.toPlayer().getDetails().getLastLocation() : entity.getLocation();
		int rx = lastRegion.getRegionX();
		int ry = lastRegion.getRegionY();
		int cx = entity.getLocation().getRegionX();
		int cy = entity.getLocation().getRegionY();
		if ((rx - cx) >= 4) {
			entity.putAttribute(AttributeKey.MAP_REGION_CHANGED, true);
		} else if ((rx - cx) <= -4) {
			entity.putAttribute(AttributeKey.MAP_REGION_CHANGED, true);
		}
		if ((ry - cy) >= 4) {
			entity.putAttribute(AttributeKey.MAP_REGION_CHANGED, true);
		} else if ((ry - cy) <= -4) {
			entity.putAttribute(AttributeKey.MAP_REGION_CHANGED, true);
		}
		if (entity.getAttribute(AttributeKey.MAP_REGION_CHANGED, false)) {
			if (walkPoint != null) {
				walkingQueue.addFirst(walkPoint);
				walkDir = -1;
			}
			if (runPoint != null) {
				walkingQueue.addFirst(runPoint);
				runDir = -1;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Resets the walking queue.
	 *
	 * @param running
	 * 		The running flag (ctrl + click action).
	 */
	public void reset(boolean running) {
		walkingQueue.clear();
		walkingQueue.add(new Point(entity.getLocation().getX(), entity.getLocation().getY(), null));
		this.running = running;
	}
	
	/**
	 * Adds a path to the walking queue.
	 *
	 * @param x
	 * 		The last x-coordinate of the path.
	 * @param y
	 * 		The last y-coordinate of the path.
	 */
	public void addPath(int x, int y) {
		/*
		 * The RuneScape client will not send all the points in the queue. It just sends places where the direction changes.
		 *
		 * For instance, walking from a route like this:
		 *
		 * <code> ***** * * ***** </code>
		 *
		 * Only the places marked with X will be sent:
		 *
		 * <code> X***X * * X***X </code>
		 *
		 * This code will 'fill in' these points and then add them to the queue.
		 */
		Point point = walkingQueue.peekLast();
		int diffX = 0, diffY = 0;
		if (point != null) {
			if (point.x != -1 && x != -1) {
				diffX = x - point.x;
				diffY = y - point.y;
			}
		}
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int i = 0; i < max; i++) {
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
			addPoint(x - diffX, y - diffY);
		}
	}
	
	/**
	 * Adds a point to the walking queue.
	 *
	 * @param x
	 * 		The x-coordinate of the point.
	 * @param y
	 * 		The y-coordinate of the point.
	 */
	public void addPoint(int x, int y) {
		Point point = walkingQueue.peekLast();
		int diffX = x - point.x, diffY = y - point.y;
		WalkingDirection direction = Directions.directionFor(diffX, diffY);
		if (direction != null) {
			walkingQueue.add(new Point(x, y, direction));
		}
	}
	
	/**
	 * Resets the walking queue.
	 */
	public void reset() {
		reset(running);
	}
	
	/**
	 * Represents a single point to walk.
	 *
	 * @author Mystic Flow
	 * @author Graham
	 */
	public static class Point {
		
		/**
		 * The x-coordinate.
		 */
		private final int x;
		
		/**
		 * The y-coordinate.
		 */
		private final int y;
		
		/**
		 * The direction.
		 */
		private final WalkingDirection direction;
		
		/**
		 * Constructs a new {@code Point} {@code Object}.
		 *
		 * @param x
		 * 		The x-coordinate.
		 * @param y
		 * 		The y-coordinate.
		 * @param direction
		 * 		The walking direction.
		 */
		private Point(int x, int y, WalkingDirection direction) {
			this.x = x;
			this.y = y;
			this.direction = direction;
		}
		
		/**
		 * Gets the x-coordinate of this point.
		 *
		 * @return The x-coordinate.
		 */
		public int getX() {
			return x;
		}
	}
	
}
package org.redrune.game.node.entity;

import lombok.Getter;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.render.flag.impl.TeleportUpdate;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.Misc;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public class EntityMovement {
	
	/**
	 * The steps to walk to
	 */
	private final ConcurrentLinkedQueue<int[]> walkSteps;
	
	/**
	 * The entity
	 */
	private final Entity entity;
	
	/**
	 * The next walk direction
	 */
	@Getter
	private int nextWalkDirection;
	
	/**
	 * The next run direction
	 */
	@Getter
	private int nextRunDirection;
	
	EntityMovement(Entity entity) {
		this.entity = entity;
		this.walkSteps = new ConcurrentLinkedQueue<>();
	}
	
	/**
	 * Processes the movement
	 */
	public void processMovement() {
		nextWalkDirection = nextRunDirection = -1;
		if (updateTeleport()) {
			return;
		}
		if (walkSteps.isEmpty()) {
			return;
		}
		nextWalkDirection = getNextWalkStep();
		if (nextWalkDirection != -1) {
			moveLocation(RegionManager.DIRECTION_DELTA_X[nextWalkDirection], RegionManager.DIRECTION_DELTA_Y[nextWalkDirection]);
			if (isRunning()) {
				nextRunDirection = getNextWalkStep();
				if (nextRunDirection != -1) {
					moveLocation(RegionManager.DIRECTION_DELTA_X[nextRunDirection], RegionManager.DIRECTION_DELTA_Y[nextRunDirection]);
				}
			}
		}
		RegionManager.updateEntityRegion(entity);
		if (entity.needsMapUpdate()) {
			entity.loadMapRegions();
		}
	}
	
	/**
	 * Checks if the player is teleporting, if so does the teleporting and
	 * returns true.
	 *
	 * @return {@code True} if the player is teleporting, {@code false} if not.
	 */
	private boolean updateTeleport() {
		if (entity.getAttribute(AttributeKey.TELEPORT_LOCATION) != null) {
			resetWalkSteps();
			entity.setLocation(entity.getAttribute(AttributeKey.TELEPORT_LOCATION));
			RegionManager.updateEntityRegion(entity);
			entity.removeAttribute(AttributeKey.TELEPORT_LOCATION);
			entity.getUpdateMasks().register(new TeleportUpdate());
			if (entity.needsMapUpdate()) {
				entity.loadMapRegions();
			}
			entity.putAttribute(AttributeKey.PLAYER_TELEPORTED, true);
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the next walk step
	 */
	private int getNextWalkStep() {
		int step[] = walkSteps.poll();
		if (step == null) {
			return -1;
		}
		return step[0];
	}
	
	/**
	 * Moves the entity to a location
	 *
	 * @param xOffset
	 * 		The x offset
	 * @param yOffset
	 * 		The y offset
	 */
	private void moveLocation(int xOffset, int yOffset) {
		entity.setLocation(entity.getLocation().transform(xOffset, yOffset, 0));
	}
	
	/**
	 * Checks if the entity is running.
	 *
	 * @return {@code True} if a ctrl + click action was performed, <br> the player has the run option enabled or the
	 * NPC is a familiar, <p> {@code false} if not.
	 */
	public boolean isRunning() {
		return entity.isNPC() || (entity.isPlayer() && entity.toPlayer().getVariables().isRunToggled());
	}
	
	/**
	 * Resets the walk steps
	 */
	public void resetWalkSteps() {
		walkSteps.clear();
	}
	
	/**
	 * Adds a walk step
	 *
	 * @param destX
	 * 		The step x
	 * @param destY
	 * 		The step y
	 * @param maxStepsCount
	 * 		The max steps to add
	 * @param check
	 * 		If we should check the tiles for masks
	 */
	public boolean addWalkSteps(final int destX, final int destY, int maxStepsCount, boolean check) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			if (!addWalkStep(myX, myY, lastTile[0], lastTile[1], check)) {
				return false;
			}
			if (stepCount == maxStepsCount) {
				return true;
			}
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY) {
				return true;
			}
		}
	}
	
	/**
	 * Gets the last walk tile
	 */
	private int[] getLastWalkTile() {
		Object[] objects = walkSteps.toArray();
		if (objects.length == 0) {
			return new int[] { entity.getLocation().getX(), entity.getLocation().getY() };
		}
		int step[] = (int[]) objects[objects.length - 1];
		return new int[] { step[1], step[2] };
	}
	
	/**
	 * Adds a walk step
	 *
	 * @param nextX
	 * 		The step x
	 * @param nextY
	 * 		The step y
	 * @param lastX
	 * 		The last x
	 * @param lastY
	 * 		The last y
	 * @param check
	 * 		If we should check the tiles
	 */
	private boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
		int dir = Misc.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1) {
			return false;
		}
		if (check && !RegionManager.isTileFree(entity.getLocation().getPlane(), lastX, lastY, dir, entity.getSize())) {
			return false;
		}
		walkSteps.add(new int[] { dir, nextX, nextY });
		return true;
	}
	
	/**
	 * Checks if a projectile can travel to the tile
	 *
	 * @param tile
	 * 		The tile
	 * @param checkClose
	 * 		If we should check close-by tiles
	 * @param size
	 * 		The size of the projectile
	 */
	public boolean clippedProjectile(Location tile, boolean checkClose, int size) {
		int myX = entity.getLocation().getX();
		int myY = entity.getLocation().getY();
		if (entity.isNPC() && size == 1) {
			NPC n = (NPC) entity;
			Location thist = n.getMiddleWorldTile();
			myX = thist.getX();
			myY = thist.getY();
		}
		int destX = tile.getX();
		int destY = tile.getY();
		int lastTileX = myX;
		int lastTileY = myY;
		while (true) {
			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			int dir = Misc.getMoveDirection(myX - lastTileX, myY - lastTileY);
			if (dir == -1) {
				return false;
			}
			if (checkClose) {
				if (!RegionManager.isTileFree(entity.getLocation().getPlane(), lastTileX, lastTileY, dir, size)) {
					return false;
				}
			} else if (!RegionManager.checkProjectileStep(entity.getLocation().getPlane(), lastTileX, lastTileY, dir, size)) {
				return false;
			}
			lastTileX = myX;
			lastTileY = myY;
			if (lastTileX == destX && lastTileY == destY) {
				return true;
			}
		}
	}
	
	/**
	 * Resets the steps and sets the player to run
	 *
	 * @param forceRun
	 * 		If the player should run
	 */
	public void reset(boolean forceRun) {
		if (forceRun && entity.isPlayer()) {
			entity.toPlayer().getVariables().setRunToggled(true);
			entity.toPlayer().sendSettings();
		}
		resetWalkSteps();
	}
	
	/**
	 * Checks if the entity is moving
	 */
	public boolean isMoving() {
		return nextWalkDirection != -1 || nextRunDirection != -1 || hasWalkSteps();
	}
	
	/**
	 * Checks if we have walk steps
	 */
	public boolean hasWalkSteps() {
		return !walkSteps.isEmpty();
	}
	
	@Override
	public String toString() {
		return "[walk=" + nextWalkDirection + ", run=" + nextRunDirection + ", steps=" + walkSteps + "]";
	}
	
	/**
	 * Adds walk steps with -1 max steps and checking tiles.
	 *
	 * @param destX
	 * 		The x
	 * @param destY
	 * 		The y
	 */
	public boolean addWalkSteps(final int destX, final int destY) {
		return addWalkSteps(destX, destY, -1, true);
	}
}

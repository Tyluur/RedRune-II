package org.redrune.game.world.path;

import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.utility.rs.Position;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class PathFactory {
	
	/**
	 * The instance of the path factory
	 */
	private static PathFactory singleton = null;
	
	/**
	 * Gets the instance of this class
	 */
	public static PathFactory get() {
		if (singleton == null) {
			singleton = new PathFactory();
		}
		return singleton;
	}
	
	/**
	 * Does a path
	 *
	 * @param pathFinder
	 * 		The path finder
	 * @param entity
	 * 		The entity
	 * @param x
	 * 		The x coordinate
	 * @param y
	 * 		The y coordinate
	 */
	public PathState doPath(PathFinder pathFinder, Entity entity, int x, int y) {
		return doPath(pathFinder, entity, x, y, false, true, true);
	}
	
	/**
	 * Does a path
	 *
	 * @param pathFinder
	 * 		The path finder
	 * @param entity
	 * 		The entity
	 * @param x
	 * 		The x
	 * @param y
	 * 		The y
	 * @param ignoreLastStep
	 * 		If we should ignore the last step.
	 * @param addToWalking
	 * 		If the path should add points to the walking queue
	 * @param moveNear
	 * 		If we should move near if we can't find a path to the destination.
	 */
	public PathState doPath(final PathFinder pathFinder, final Entity entity, final int x, final int y, final boolean ignoreLastStep, boolean addToWalking, boolean moveNear) {
		Location destination = Location.create(x, y, entity.getLocation().getPlane());
		Location base = entity.getLocation();
		int srcX = entity.getLocation().getViewportX(0);
		int srcY = entity.getLocation().getViewportY(0);
		int destX = destination.getViewportX(base, 0);
		int destY = destination.getViewportY(base, 0);
		PathState state = pathFinder.findPath(entity, entity.getLocation(), srcX, srcY, destX, destY, entity.getLocation().getPlane(), 0, entity.getWalkingQueue().isRunning(), ignoreLastStep, moveNear);
		if (state == null || !addToWalking) {
			return state;
		}
		entity.getWalkingQueue().reset();
		for (Position step : state.getPoints()) {
			entity.getWalkingQueue().addPath(step.getX(), step.getY());
		}
		return state;
	}
	
	/**
	 * Does a path
	 *
	 * @param pathFinder
	 * 		The path finder
	 * @param entity
	 * 		The entity
	 * @param x
	 * 		The x coordinate
	 * @param y
	 * 		The y coordinate
	 * @param ignoreLastStep
	 * 		U
	 */
	public PathState doPath(final PathFinder pathFinder, Entity entity, final int x, final int y, final boolean ignoreLastStep, boolean addToWalking) {
		return doPath(pathFinder, entity, x, y, ignoreLastStep, addToWalking, true);
	}
	
	/**
	 * Does a path using points
	 *
	 * @param entity
	 * 		The entity
	 * @param state
	 * 		The state
	 */
	public void doPath(Entity entity, PathState state) {
		if (state != null) {
			entity.getWalkingQueue().reset();
			for (Position step : state.getPoints()) {
				entity.getWalkingQueue().addPoint(step.getX(), step.getY());
			}
		}
	}
}

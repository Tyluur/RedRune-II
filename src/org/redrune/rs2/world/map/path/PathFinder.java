package org.redrune.rs2.world.map.path;

import org.redrune.rs2.node.entity.Entity;
import org.redrune.rs2.world.map.Location;

/**
 * The pathfinder interface.
 *
 * @author Graham
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public interface PathFinder {
	
	int SOUTH_FLAG = 0x1, WEST_FLAG = 0x2, NORTH_FLAG = 0x4, EAST_FLAG = 0x8;
	
	int SOUTH_WEST_FLAG = SOUTH_FLAG | WEST_FLAG;
	
	int NORTH_WEST_FLAG = NORTH_FLAG | WEST_FLAG;
	
	int SOUTH_EAST_FLAG = SOUTH_FLAG | EAST_FLAG;
	
	int NORTH_EAST_FLAG = NORTH_FLAG | EAST_FLAG;
	
	int SOLID_FLAG = 0x20000;
	
	int UNKNOWN_FLAG = 0x40000000;
	
	/**
	 * Finds a path for the entity.
	 *
	 * @param mob
	 * 		The entity.
	 * @param base
	 * 		The base location.
	 * @param srcX
	 * 		The source x-coordinate.
	 * @param srcY
	 * 		The source y-coordinate.
	 * @param dstX
	 * 		The destination x-coordinate.
	 * @param dstY
	 * 		The destination y-coordinate.
	 * @param z
	 * 		The height.
	 * @param radius
	 * 		The radius.
	 * @param running
	 * 		If the entity should run.
	 * @param ignoreLastStep
	 * 		If we should ignore the last step.
	 * @param moveNear
	 * 		If we should move near if we can't find a path to the destination.
	 * @return The path state.
	 */
	PathState findPath(Entity mob, Location base, int srcX, int srcY, int dstX, int dstY, int z, int radius, boolean running, boolean ignoreLastStep, boolean moveNear);
	
}

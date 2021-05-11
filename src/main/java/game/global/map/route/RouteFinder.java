package game.global.map.route;

import game.entity.actor.Actor;
import game.global.WorldTile;
import utility.functions.Misc;

/**
 * Route finder, designed for single-threaded usage.
 *
 * @author Mangis
 */
public class RouteFinder {
	
	/**
	 * Standart walk route finder type.
	 */
	public static final int WALK_ROUTEFINDER = 0;
	
	/**
	 * Last routefinder that was used.
	 */
	private static int lastUsed;
	
	/**
	 * Find's route using given strategy. Returns amount of steps found. If steps > 0, route exists. If steps = 0, route
	 * exists, but no need to move. If steps < 0, route does not exist.
	 */
	public static int findRoute(int type, int srcX, int srcY, int srcZ, int srcSizeXY, RouteStrategy strategy, boolean findAlternative) {
		switch (lastUsed = type) {
			case WALK_ROUTEFINDER:
				return WalkRouteFinder.findRoute(srcX, srcY, srcZ, srcSizeXY, strategy, findAlternative);
			default:
				throw new RuntimeException("Unknown routefinder type.");
		}
	}
	
	/**
	 * Get's last path buffer x. Modifying the buffer in any way is prohibited.
	 */
	public static int[] getLastPathBufferX() {
		switch (lastUsed) {
			case WALK_ROUTEFINDER:
				return WalkRouteFinder.getLastPathBufferX();
			default:
				throw new RuntimeException("Unknown routefinder type.");
		}
	}
	
	/**
	 * Get's last path buffer y. Modifying the buffer in any way is prohibited.
	 */
	public static int[] getLastPathBufferY() {
		switch (lastUsed) {
			case WALK_ROUTEFINDER:
				return WalkRouteFinder.getLastPathBufferY();
			default:
				throw new RuntimeException("Unknown routefinder type.");
		}
	}
	
	/**
	 * Whether last path is only alternative path.
	 */
	public static boolean lastIsAlternative() {
		switch (lastUsed) {
			case WALK_ROUTEFINDER:
				return WalkRouteFinder.lastIsAlternative();
			default:
				throw new RuntimeException("Unknown routefinder type.");
		}
	}
	
	public static boolean findBasicRoute(Actor src, WorldTile dest, int maxStepsCount, boolean calculate) {
		int[] srcPos = src.getLastWalkTile();
		int[] destPos = { dest.getX(), dest.getY() };
		int srcSize = src.getSize();
		//set destSize to 0 to walk under it else follows
		int destSize = dest instanceof Actor ? ((Actor) dest).getSize() : 1;
		int[] destScenePos = { destPos[0] + destSize - 1, destPos[1] + destSize - 1 };//Arrays.copyOf(destPos, 2);//destSize == 1 ? Arrays.copyOf(destPos, 2) : new int[] {WorldTile.getCoordFaceX(destPos[0], destSize, destSize, -1), WorldTile.getCoordFaceY(destPos[1], destSize, destSize, -1)};
		while (maxStepsCount-- != 0) {
			int[] srcScenePos = { srcPos[0] + srcSize - 1, srcPos[1] + srcSize - 1 };//srcSize == 1 ? Arrays.copyOf(srcPos, 2) : new int[] { WorldTile.getCoordFaceX(srcPos[0], srcSize, srcSize, -1), WorldTile.getCoordFaceY(srcPos[1], srcSize, srcSize, -1)};
			if (!Misc.isOnRange(srcPos[0], srcPos[1], srcSize, destPos[0], destPos[1], destSize, 0)) {
				if (srcScenePos[0] < destScenePos[0] && srcScenePos[1] < destScenePos[1] && src.addWalkStep(srcPos[0] + 1, srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[0] > destScenePos[0] && srcScenePos[1] > destScenePos[1] && src.addWalkStep(srcPos[0] - 1, srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					srcPos[1]--;
					continue;
				}
				if (srcScenePos[0] < destScenePos[0] && srcScenePos[1] > destScenePos[1] && src.addWalkStep(srcPos[0] + 1, srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					srcPos[1]--;
					continue;
				}
				if (srcScenePos[0] > destScenePos[0] && srcScenePos[1] < destScenePos[1] && src.addWalkStep(srcPos[0] - 1, srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[0] < destScenePos[0] && src.addWalkStep(srcPos[0] + 1, srcPos[1], srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					continue;
				}
				if (srcScenePos[0] > destScenePos[0] && src.addWalkStep(srcPos[0] - 1, srcPos[1], srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					continue;
				}
				if (srcScenePos[1] < destScenePos[1] && src.addWalkStep(srcPos[0], srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[1] > destScenePos[1] && src.addWalkStep(srcPos[0], srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[1]--;
					continue;
				}
				return false;
			}
			break; //for now nothing between break and return
		}
		return true;
	}
}

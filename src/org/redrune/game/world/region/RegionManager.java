package org.redrune.game.world.region;

import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.item.FloorItem;
import org.redrune.utility.rs.constant.ClippingFlags;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @author Emperor
 * @author Dementhium development team (mainly).
 * @since 5/26/2017
 */
public class RegionManager {
	
	/**
	 * The direction deltas, different from the ones in {@link Location}.
	 */
	private static final byte[] DIRECTION_DELTA_Y = new byte[] { 1, 1, 1, 0, 0, -1, -1, -1 };
	
	/**
	 * The direction deltas, different from the ones in {@link Location}.
	 */
	private static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1, 1, -1, 0, 1 };
	
	/**
	 * The region mapping.
	 */
	private static final Map<Integer, Region> REGION_CACHE = new ConcurrentHashMap<>();
	
	/**
	 * When an entity enters a new region, we must add them to the new region, remove them from the previous one as
	 * well.
	 *
	 * @param entity
	 * 		The entity.
	 */
	// TODO: multi-zone support
	// TODO: region-music support
	public static void updateEntityRegion(Entity entity) {
		if (!entity.isRenderable()) {
			entity.getRegion().removeEntity(entity);
			return;
		}
		int regionId = entity.getRegion().getId();
		int lastRegionId = entity.getLastRegion() == null ? 0 : entity.getLastRegion().getId();
		
		// change of region
		if (lastRegionId != regionId) {
			if (lastRegionId > 0) {
				getRegion(lastRegionId).removeEntity(entity);
			}
			Region region = getRegion(regionId);
			region.addEntity(entity);
			entity.setLastRegion(entity.getRegion());
		}
		// this is where we would check if we are in a multi zone
	}
	
	/**
	 * Gets a new region by the id
	 *
	 * @param regionId
	 * 		The id of the region
	 */
	public static Region getRegion(int regionId) {
		return REGION_CACHE.computeIfAbsent(regionId, Region::new);
	}
	
	/**
	 * Checks if a tile is free
	 *
	 * @param plane
	 * 		The plane of the tile
	 * @param x
	 * 		The x
	 * @param y
	 * 		The y
	 * @param size
	 * 		The size of the node checking.
	 */
	public static boolean isTileFree(int plane, int x, int y, int dir, int size) {
		return isTileFree(plane, x, y, DIRECTION_DELTA_X[dir], DIRECTION_DELTA_Y[dir], size);
	}
	
	/**
	 * Checks if a tile is free
	 *
	 * @param plane
	 * 		The plane of the tile
	 * @param x
	 * 		The x
	 * @param y
	 * 		The y
	 * @param xOffset
	 * 		The x offset, based on direction
	 * @param yOffset
	 * 		The y offset, based on direction
	 * @param size
	 * 		The size of the node checking.
	 */
	public static boolean isTileFree(int plane, int x, int y, int xOffset, int yOffset, int size) {
		if (size == 1) {
			int mask = getClippingMask(x + xOffset, y + yOffset, plane);
			if (xOffset == -1 && yOffset == 0) {
				return (mask & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_EAST)) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (mask & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_WEST)) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (mask & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_NORTH)) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (mask & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_SOUTH)) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (mask & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_NORTH | ClippingFlags.WALLOBJ_EAST | ClippingFlags.CORNEROBJ_NORTHEAST)) == 0 && (getClippingMask(x - 1, y, plane) & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_EAST)) == 0 && (getClippingMask(x, y - 1, plane) & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_NORTH)) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (mask & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_NORTH | ClippingFlags.WALLOBJ_WEST | ClippingFlags.CORNEROBJ_NORTHWEST)) == 0 && (getClippingMask(x + 1, y, plane) & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_WEST)) == 0 && (getClippingMask(x, y - 1, plane) & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_NORTH)) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (mask & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_EAST | ClippingFlags.WALLOBJ_SOUTH | ClippingFlags.CORNEROBJ_SOUTHEAST)) == 0 && (getClippingMask(x - 1, y, plane) & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_EAST)) == 0 && (getClippingMask(x, y + 1, plane) & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_SOUTH)) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (mask & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_SOUTH | ClippingFlags.WALLOBJ_WEST | ClippingFlags.CORNEROBJ_SOUTHWEST)) == 0 && (getClippingMask(x + 1, y, plane) & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_WEST)) == 0 && (getClippingMask(x, y + 1, plane) & (ClippingFlags.FLOOR_BLOCKSWALK | ClippingFlags.FLOORDECO_BLOCKSWALK | ClippingFlags.OBJ | ClippingFlags.WALLOBJ_SOUTH)) == 0;
			}
		}
		return false;
	}
	
	/**
	 * Gets the clipping mask for the given coordinates.
	 *
	 * @param x
	 * 		The x-coordinate.
	 * @param y
	 * 		The y-coordinate.
	 * @param z
	 * 		The height.
	 * @return The clipping mask.
	 */
	public static int getClippingMask(int x, int y, int z) {
		Region region = getRegion(x, y);
		if (region.getClippingMasks()[z] == null) {
			return -1;
		}
		/*TODO: if (region.getClippingMasks()[z] == null || !region.isClipped()) {
			DynamicRegion dynamicRegion = RegionBuilder.getDynamicRegion(x, y);
			if (dynamicRegion != null) {
				int baseLocalX = x - (((x >> 3) >> 3) << 6) ;
				int baseLocalY = y - (((y >> 3) >> 3) << 6);
				return dynamicRegion.getMask(z, baseLocalX, baseLocalY);
			}
			return -1;
		}*/
		int localX = x - ((x >> 7) << 7);
		int localY = y - ((y >> 7) << 7);
		return region.getClippingMasks()[z][localX][localY];
	}
	
	/**
	 * Gets a region.
	 *
	 * @param x
	 * 		The x-coordinate.
	 * @param y
	 * 		The y-coordinate.
	 * @return The region.
	 */
	static Region getRegion(int x, int y) {
		int regionId = Location.getRegionId(x, y);
		return REGION_CACHE.computeIfAbsent(regionId, k -> new Region(regionId));
	}
	
	/**
	 * Adds a public floor item
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param itemAmount
	 * 		The amount of the item
	 * @param targetTicks
	 * 		The ticks until the next item phase is hit
	 * @param location
	 * 		The location of the item
	 */
	public static void addPublicFloorItem(int itemId, int itemAmount, int targetTicks, Location location) {
		addFloorItem(itemId, itemAmount, targetTicks, location, null);
	}
	
	/**
	 * Adds a floor item to the region
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param itemAmount
	 * 		The amount of the item
	 * @param targetTicks
	 * 		The ticks until the next item phase is hit
	 * @param location
	 * 		The location of the item
	 * @param ownerUsername
	 * 		The name of the user who owns the item
	 */
	public static void addFloorItem(int itemId, int itemAmount, int targetTicks, Location location, String ownerUsername) {
		Region region = getRegion(location.getRegionId());
		FloorItem item = new FloorItem(ownerUsername, targetTicks, itemId, itemAmount, location);
		if (!region.addFloorItemToList(item)) {
			throw new IllegalStateException("Unable to add floor item to region list.");
		}
		region.handleAddition(item);
	}
	
}
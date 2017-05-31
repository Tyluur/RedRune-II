package org.redrune.game.world.region;

import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;

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
	 * The region mapping.
	 */
	private static final Map<Integer, Region> REGION_CACHE = new ConcurrentHashMap<>();
	
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
	
}
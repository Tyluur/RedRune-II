package org.redrune.cache;

import org.redrune.cache.parse.BodyDataParser;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.game.GameConstants;
import org.redrune.game.world.region.RegionDeletion;
import org.redrune.utility.Misc;
import org.redrune.utility.backend.MapDataParser;

import java.util.logging.Logger;

/**
 * The cache loading class
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/17
 */
public class Cache {
	
	/**
	 * The instance of the logger
	 */
	private static final Logger logger = Misc.constructLogger(Cache.class);
	
	/**
	 * Initializes the cache
	 */
	public static void init() {
		try {
			CacheManager.load(GameConstants.CACHE_PATH);
			BodyDataParser.loadAll();
			ItemDefinitionParser.loadEquipmentConfiguration();
			MapDataParser.readAll();
			RegionDeletion.prepare();
			logger.info("Cache loaded! [items=" + getAmountOfItems() + ", interfaces=" + getAmountOfInterfaces() + ", npcs=" + getAmountOfNpcs() + ", objects=" + getAmountOfObjects() + ", anims=" + getAmountOfAnimations() + ", graphics=" + getAmountOfGraphics() + "]");
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Gets the amount of items
	 *
	 * @return An {@code Integer} {@code Object}
	 */
	public static int getAmountOfItems() {
		return CacheManager.cacheCFCount(CacheConstants.ITEMDEF_IDX_ID);
	}
	
	/**
	 * Gets the amount of interfaces
	 *
	 * @return An {@code Integer} {@code Object}
	 */
	public static int getAmountOfInterfaces() {
		return CacheManager.containerCount(CacheConstants.INTERFACEDEF_IDX_ID);
	}
	
	/**
	 * Gets the amount of npcs
	 *
	 * @return An {@code Integer} {@code Object}
	 */
	public static int getAmountOfNpcs() {
		return CacheManager.cacheCFCount2(CacheConstants.NPCDEF_IDX_ID);
	}
	
	/**
	 * Gets the amount of objects
	 *
	 * @return An {@code Integer} {@code Object}
	 */
	public static int getAmountOfObjects() {
		return CacheManager.cacheCFCount(CacheConstants.OBJECTDEF_IDX_ID);
	}
	
	/**
	 * Gets the amount of animations
	 *
	 * @return An {@code Integer} {@code Object}
	 */
	public static int getAmountOfAnimations() {
		return CacheManager.cacheCFCount2(CacheConstants.ANIM_IDX_ID);
	}
	
	/**
	 * Gets the amount of gfxes
	 *
	 * @return An {@code Integer} {@code Object}
	 */
	public static int getAmountOfGraphics() {
		return CacheManager.cacheCFCount(CacheConstants.GFX_IDX_ID);
	}
	
	/**
	 * Gets the amount of components an interface has
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public static int getAmountOfComponents(int interfaceId) {
		return CacheManager.getContainerChildCount(CacheConstants.INTERFACEDEF_IDX_ID, interfaceId);
	}
	
}

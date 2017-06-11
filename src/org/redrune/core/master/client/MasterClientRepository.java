package org.redrune.core.master.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class MasterClientRepository {
	
	/**
	 * The list of worlds
	 */
	private static final Map<Integer, MasterClientWorld> worldMap = new ConcurrentHashMap<>();
	
	/**
	 * Updates the world
	 *
	 * @param worldId
	 * 		The id of the world
	 * @param size
	 * 		The amount of players online
	 * @param isOnline
	 * 		If the world is online
	 */
	public static void updateWorld(int worldId, int size, boolean isOnline) {
		MasterClientWorld world = worldMap.computeIfAbsent(worldId, MasterClientWorld::new);
		world.setSize(size);
		world.setOnline(isOnline);
	}
	
	/**
	 * Gets the amount of players on the world
	 *
	 * @param worldId
	 * 		The world
	 */
	public static int getPlayerCount(int worldId) {
		MasterClientWorld world = worldMap.get(worldId);
		if (world == null) {
			return 0;
		}
		return world.getSize();
	}
	
	/**
	 * Checks if a world is online
	 *
	 * @param worldId
	 * 		The id of the world
	 */
	public static boolean isOnline(int worldId) {
		MasterClientWorld world = worldMap.get(worldId);
		return world != null && world.isOnline();
	}
	
}

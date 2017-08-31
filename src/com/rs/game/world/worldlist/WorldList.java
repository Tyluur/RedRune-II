package com.rs.game.world.worldlist;

import java.util.HashMap;

import static com.rs.game.world.worldlist.WorldListConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/30/2017
 */
public class WorldList {
	
	private static final HashMap<Integer, WorldEntry> WORLDS = new HashMap<>();
	
	// String activity, String ip, int countryId, String countryName, boolean
	// members
	public static void init() {
		WORLDS.put(1, new WorldEntry("Game Server", "127.0.0.1", 38, FLAG_MEMBERS | FLAG_LOOTSHARE | FLAG_HIGHLIGHT, "Canada", true));
		WORLDS.put(2, new WorldEntry("PvP World", "127.0.0.1", 38, FLAG_MEMBERS | FLAG_LOOTSHARE | FLAG_HIGH_RISK, "Canada", true));
	}
	
	/**
	 * Gets all the worlds
	 */
	public static HashMap<Integer, WorldEntry> getWorlds() {
		return WORLDS;
	}
	
}

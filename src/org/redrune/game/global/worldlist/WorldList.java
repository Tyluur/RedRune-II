package org.redrune.game.global.worldlist;

import java.util.HashMap;

import static org.redrune.game.global.worldlist.WorldListConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/30/2017
 */
public class WorldList {
	
	/**
	 * The map of world entries
	 */
	private static final HashMap<Integer, WorldEntry> WORLDS = new HashMap<>();
	
	/**
	 * Registers all worlds
	 */
	public static void initialize() {
		WORLDS.put(1, new WorldEntry("Game Server", "127.0.0.1", 38, FLAG_MEMBERS | FLAG_LOOTSHARE | FLAG_HIGHLIGHT, "Canada", true));
		WORLDS.put(2, new WorldEntry("PvP World", "127.0.0.1", 38, FLAG_MEMBERS | FLAG_LOOTSHARE | FLAG_HIGH_RISK, "Canada", true));
		System.out.println("Loaded " + WORLDS.size() + " worlds");
	}
	
	/**
	 * Gets all the worlds
	 */
	public static HashMap<Integer, WorldEntry> getWorlds() {
		return WORLDS;
	}
	
}

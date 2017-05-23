package org.redrune.rs2.world.connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all the current worlds.
 *
 * @author Dementhium development team
 */
public class WorldList {
	
	/**
	 * A list holding all the currently loaded worlds.
	 */
	private static final List<WorldDefinition> WORLD_LIST = new ArrayList<>();
	
	/* Populates the world list. */
	static {
		WORLD_LIST.add(new WorldDefinition(1, WorldConstants.COUNTRY_CANADA, WorldConstants.FLAG_MEMBERS, "World 1", "127.0.0.1", "USA", WorldConstants.COUNTRY_CANADA));
		WORLD_LIST.add(new WorldDefinition(2, WorldConstants.COUNTRY_CANADA, WorldConstants.FLAG_PVP, "PvP World", "127.0.0.1", "USA", WorldConstants.COUNTRY_CANADA));
	}
	
	/**
	 * Gets the world list
	 */
	public static List<WorldDefinition> getWorldList() {
		return WORLD_LIST;
	}
	
}
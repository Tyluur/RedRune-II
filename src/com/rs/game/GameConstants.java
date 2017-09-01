package com.rs.game;

import com.rs.game.entity.WorldTile;

/**
 * All constants for the game are stored here
 */
public interface GameConstants {
	
	// Server Connection Settings
	
	/**
	 * The name of the server
	 */
	String SERVER_NAME = "RedRune";
	
	//	public static final String UPDATE = "N/A";
	
	/**
	 * The path that the cache is at
	 */
	String CACHE_PATH = "data/cache/";
	
	/**
	 * The location that players spawn at
	 */
	WorldTile START_PLAYER_LOCATION = new WorldTile(3092, 3503, 0);
	
	/**
	 * The location that players who die spawn at
	 */
	WorldTile RESPAWN_PLAYER_LOCATION = new WorldTile(3102, 3492, 0);
	
	/**
	 * The maximum amount of players online
	 */
	int PLAYERS_LIMIT = 2000;
	
	/**
	 * The maximum amount of npcs online
	 */
	int NPCS_LIMIT = Short.MAX_VALUE;
	
	/**
	 * The maximum amount of npcs we can see
	 */
	int LOCAL_NPCS_LIMIT = 1000;
	
	/**
	 * The {@link Runtime#freeMemory()} cap
	 */
	int MIN_FREE_MEM_ALLOWED = 30000000;
	
	/**
	 * The controller players get on creation
	 */
	String DEFAULT_CONTROLLER = "";
}

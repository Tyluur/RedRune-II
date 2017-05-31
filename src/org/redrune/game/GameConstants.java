package org.redrune.game;

import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public interface GameConstants {
	
	/**
	 * The name of the server
	 */
	String SERVER_NAME = "RedRune";
	
	/**
	 * The path of the cache
	 */
	String CACHE_PATH = "./data/cache/";
	
	/**
	 * The id of the port used for main communications
	 */
	int MAIN_PORT_ID = 43594;
	
	/**
	 * The maximum amount of players allowed online
	 */
	int PLAYERS_LIMIT = 2048;
	
	/**
	 * The maximum amount of npcs allowed online
	 */
	int NPCS_LIMIT = Short.MAX_VALUE;
	
	/**
	 * The home location
	 */
	Location HOME_LOCATION = new Location(3092, 3503);
	
}

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
	String CACHE_PATH = "./data/fs/";
	
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
	
	/**
	 * The location players are sent to when they die
	 */
	Location DEATH_LOCATION = new Location(3102, 3492);
	
	/**
	 * The experience multiplier for combat skills
	 */
	int COMBAT_EXPERIENCE_MULTIPLIER = 75;
	
	/**
	 * The experience multiplier for non-combat skills (excluding prayer)
	 */
	int SKILL_EXPERIENCE_MULTIPLIER = 10;
	
	/**
	 * The experience multiplier for prayer
	 */
	int PRAYER_EXPERIENCE_MULTIPLIER = 5;
	
}

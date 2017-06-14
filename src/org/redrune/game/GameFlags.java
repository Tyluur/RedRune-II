package org.redrune.game;

import com.google.common.base.Stopwatch;

/**
 * The flags that can be altered for the game are stored here.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class GameFlags {
	
	/**
	 * If the game is running on developer mode (debug)
	 */
	public static boolean debugMode;
	
	/**
	 * If the master server should be shipped with the game server as well
	 */
	public static boolean runMasterServer;
	
	/**
	 * The id of the world that will be ran
	 */
	public static int worldId;
	
	/**
	 * The instance of the stopwatch
	 */
	public static final Stopwatch STOPWATCH = Stopwatch.createUnstarted();
	
}

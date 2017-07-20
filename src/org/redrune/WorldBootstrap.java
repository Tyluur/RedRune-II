package org.redrune;

import org.redrune.game.GameServer;
import org.redrune.utility.tool.Misc;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class used to run the game
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class WorldBootstrap {
	
	/**
	 * The logger instance
	 */
	private static final Logger LOGGER = Misc.constructLogger(WorldBootstrap.class);
	
	/**
	 * The instance of the rs2 server
	 */
	private static GameServer instance;
	
	private WorldBootstrap() {
	
	}
	
	/**
	 * The main method executed from the JVM
	 *
	 * @param args
	 * 		Program arguments
	 */
	public static void main(String[] args) {
		try {
			GameServer server = get(args);
			server.start();
			server.execute();
			server.end();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unexpected error on initialization", e);
			System.exit(0);
		}
	}
	
	/**
	 * Gets the singleton instance
	 *
	 * @param args
	 * 		The arguments if we must create it
	 */
	public static GameServer get(String[] args) {
		if (instance == null) {
			synchronized (GameServer.class) {
				if (instance == null) {
					instance = new GameServer(args);
				}
			}
		}
		return instance;
	}
}

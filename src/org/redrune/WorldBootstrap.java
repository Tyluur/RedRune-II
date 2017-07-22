package org.redrune;

import org.redrune.game.world.World;
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
			// create a new world
			World world = World.create(args);
			// runs the procedure
			world.run();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unexpected error on initialization", e);
			System.exit(0);
		}
	}
	
}

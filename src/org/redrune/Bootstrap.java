package org.redrune;

import com.google.common.base.Stopwatch;
import org.redrune.cache.Cache;
import org.redrune.cache.parse.BodyDataParser;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.core.system.SystemManager;
import org.redrune.game.GameConstants;
import org.redrune.game.GameFlags;
import org.redrune.game.module.ModuleRepository;
import org.redrune.network.NetworkConstants;
import org.redrune.network.rs666.packet.structure.IncomingPacketRepository;
import org.redrune.utility.Misc;
import org.redrune.utility.backend.MapDataParser;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The procedure used to initialize the first world, the update server, and the login server.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class Bootstrap {
	
	/**
	 * The logger instance
	 */
	private static final Logger LOGGER = Misc.constructLogger(Bootstrap.class);
	
	/**
	 * The instance of the stopwatch
	 */
	private static final Stopwatch STOPWATCH = Stopwatch.createUnstarted();
	
	/**
	 * The main method executed from the JVM
	 *
	 * @param args
	 * 		Program arguments
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Unexpected end of JVM arguments!");
			System.err.println("args[0]=[true/false] - debug mode");
			return;
		}
		try {
			// startup necessities
			Bootstrap.STOPWATCH.start();
			GameFlags.debugMode = Boolean.parseBoolean(args[0]);
			SystemManager.setDefaults();
			
			// loading the actual important data
			Cache.init();
			BodyDataParser.loadAll();
			ItemDefinitionParser.loadEquipIds();
			IncomingPacketRepository.storeAll();
			ModuleRepository.registerAllModules();
			MapDataParser.readAll();
			
			// finalization
			SystemManager.start();
			LOGGER.info("Successfully started " + GameConstants.SERVER_NAME + " #" + NetworkConstants.REVISION + " in " + STOPWATCH.elapsed(TimeUnit.MILLISECONDS) + " ms.");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unexpected error on initialization - " + e);
		}
	}
	
	/**
	 * Gets the instance of the stopwatch, that has been started since the server was booted.
	 */
	public static Stopwatch getStopwatch() {
		return STOPWATCH;
	}
}

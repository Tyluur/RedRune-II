package org.redrune;

import org.redrune.cache.Cache;
import org.redrune.core.system.SystemManager;
import org.redrune.game.GameConstants;
import org.redrune.game.GameFlags;
import org.redrune.game.content.dialogue.DialogueRepository;
import org.redrune.game.module.ModuleRepository;
import org.redrune.game.module.command.CommandRepository;
import org.redrune.network.NetworkConstants;
import org.redrune.network.rs666.packet.incoming.IncomingPacketRepository;
import org.redrune.utility.Misc;

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
	 * The main method executed from the JVM
	 *
	 * @param args
	 * 		Program arguments
	 */
	public static void main(String[] args) {
		try {
			// startup necessities & flags
			GameFlags.STOPWATCH.start();
			GameFlags.debugMode = Boolean.parseBoolean(args[0]);
			GameFlags.worldId = Integer.parseInt(args[1]);
			GameFlags.runMasterServer = Boolean.parseBoolean(args[2]);
		} catch (Exception e) {
			LOGGER.severe("Unexpected JVM arguments!");
			LOGGER.severe("args[0]=[true/false] - debug mode");
			LOGGER.severe("args[1]=[integer] - worldId");
			LOGGER.severe("args[2]=[true/false] - runMasterServer");
			System.exit(1);
			return;
		}
		try {
			// defaults
			SystemManager.setDefaults();
			
			// loading the actual important data
			Cache.init();
			
			IncomingPacketRepository.storeAll();
			ModuleRepository.registerAllModules();
			DialogueRepository.loadSubscriptions();
			CommandRepository.populate();
			
			// finalization
			SystemManager.start();
			LOGGER.info("Successfully started " + GameConstants.SERVER_NAME + " #" + NetworkConstants.REVISION + " [World " + GameFlags.worldId + "] in " + GameFlags.STOPWATCH.elapsed(TimeUnit.MILLISECONDS) + " ms.");
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE, "Unexpected error on initialization", t);
			System.exit(1);
		}
	}
}
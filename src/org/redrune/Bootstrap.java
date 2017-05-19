package org.redrune;

import org.redrune.cache.Cache;
import org.redrune.network.rs666.NetworkHandler;
import org.redrune.network.rs666.packet.structure.IncomingPacketRepository;
import org.redrune.rs2.GameConstants;
import org.redrune.rs2.GameFlags;
import org.redrune.utility.Misc;

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
	private static final Logger logger = Misc.constructLogger(Bootstrap.class);
	
	/**
	 * The main method executed from the JVM
	 * @param args Program arguments
 	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Unexpected end of JVM arguments!");
			System.err.println("args[0]=[true/false] - debug mode");
			return;
		}
		try {
			GameFlags.debugMode = Boolean.parseBoolean(args[0]);
			Cache.init();
			IncomingPacketRepository.storeAll();
			NetworkHandler.bind();
			logger.info("Successfully started " + GameConstants.SERVER_NAME + "!");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error on initialization - " + e);
		}
	}
}

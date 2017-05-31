package org.redrune.core.system;

import org.redrune.utility.Misc;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/22/2017
 */
public class SystemFinalization extends Thread {
	
	/**
	 * The instance of the logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(SystemFinalization.class);
	
	@Override
	public void run() {
		LOGGER.log(Level.SEVERE, "System shutdown has been invoked.");
	}
}

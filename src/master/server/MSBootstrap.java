package master.server;

import master.server.engine.MSEngineFactory;
import master.server.network.MSNetworkSystem;
import master.utility.system.SystemManager;

/**
 * The master server bootstrap, used to start the master server on its own.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/10/2017
 */
public class MSBootstrap {
	
	/**
	 * The network system
	 */
	private static final MSNetworkSystem NETWORK_SYSTEM = new MSNetworkSystem();
	
	/**
	 * The main method that starts the master server
	 *
	 * @param args
	 * 		The jvm arguments
	 */
	public static void main(String[] args) {
		SystemManager.setDefaults();
		try {
			MSEngineFactory.startUp();
			NETWORK_SYSTEM.bind();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
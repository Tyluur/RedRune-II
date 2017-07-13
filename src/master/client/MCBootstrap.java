package master.client;

import master.client.network.MCNetworkSystem;
import master.utility.system.SystemManager;

/**
 * The master client bootstrap, used to start the master client on its own.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class MCBootstrap {
	
	/**
	 * Executes the client from the jvm
	 *
	 * @param args
	 * 		The arguments
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			throw new IllegalStateException("Arguments must be: { worldId, }");
		}
		SystemManager.setDefaults();
		try {
			MCFlags.worldId = Byte.parseByte(args[0]);
			MCNetworkSystem system = new MCNetworkSystem(MCFlags.worldId);
			system.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
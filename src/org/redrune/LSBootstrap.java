package org.redrune;

import org.redrune.cache.Cache;
import org.redrune.core.system.SystemManager;
import org.redrune.network.lobby.LobbyNetwork;
import org.redrune.network.master.client.MasterCommunication;
import org.redrune.utility.backend.UnexpectedArgsException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class LSBootstrap {
	
	public static void main(String[] args) {
		try {
			SystemManager.setDefaults(args);
		} catch (UnexpectedArgsException e) {
			UnexpectedArgsException.push();
			System.exit(1);
			return;
		}
		Cache.init();
		try {
			MasterCommunication.start();
			LobbyNetwork.PACKET_REPOSITORY.storeAll();
			LobbyNetwork.bind();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}

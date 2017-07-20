package org.redrune;

import org.redrune.cache.Cache;
import org.redrune.core.system.SystemManager;
import org.redrune.network.lobby.LobbyNetwork;
import org.redrune.network.master.client.MasterCommunication;
import org.redrune.network.world.packet.incoming.IncomingPacketRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class LSBootstrap {
	
	public static void main(String[] args) {
		SystemManager.setDefaults(null);
		Cache.init();
		try {
			MasterCommunication.start();
			IncomingPacketRepository.storeAll();
			LobbyNetwork.bind();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}

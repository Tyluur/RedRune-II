package org.redrune;

import org.redrune.cache.Cache;
import org.redrune.core.system.SystemManager;
import org.redrune.network.download.DownloadNetwork;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class DSBootstrap {
	
	public static void main(String[] args) {
		SystemManager.setDefaults(null);
		Cache.init();
		try {
			DownloadNetwork.bind();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}

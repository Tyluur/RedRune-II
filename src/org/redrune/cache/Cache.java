package org.redrune.cache;

import com.alex.store.Store;
import org.redrune.game.GameConstants;

import java.io.IOException;

public final class Cache {
	
	public static Store STORE;
	
	private Cache() {
	
	}
	
	public static void initialize() throws IOException {
		STORE = new Store(GameConstants.CACHE_PATH);
	}
	
	public static byte[] generateUkeysFile() {
		return STORE.generateIndex255Archive255Current(null, null);
	}
	
}

package org.redrune.cache;

import com.alex.store.Store;
import org.redrune.game.GameConstants;

import java.io.IOException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
public final class CacheFileStore {
	
	/**
	 * The archive with all data.
	 */
	public static Store STORE;
	
	private CacheFileStore() {
	
	}
	
	public static void init() throws IOException {
		STORE = new Store(GameConstants.CACHE_PATH);
	}
	
}

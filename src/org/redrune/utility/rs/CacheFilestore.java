package org.redrune.utility.rs;

import com.alex.store.Store;
import org.redrune.game.GameConstants;

import java.io.IOException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
public final class CacheFilestore {
	
	/**
	 * The archive with all data.
	 */
	public static Store STORE;
	
	private CacheFilestore() {
	
	}
	
	public static void init() throws IOException {
//		ReferenceTable.NEW_PROTOCOL = true;
		STORE = new Store(GameConstants.CACHE_PATH);
	}
}

package org.redrune.utility.xtea;

import java.io.IOException;

import org.redrune.cache.Cache;

/**
 * XTEAChecker.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class XTEAChecker {

	private static int failed = 0;

	private static int worked = 0;

	public static final void main(String[] args) {
		try {
			Cache.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MapXTEAKeys.init();
		for (int i = 11819; i < 11820; i++) {
			checkMapDataIfWorks(i);
		}
		System.out.println("Worked=" + worked + " - Failed=" + failed);
	}

	public static void checkMapDataIfWorks(int id) {
		int[] xtea_keys = MapXTEAKeys.getKeys(id);
		int absX = (id >> 8) * 64;
		int absY = (id & 0xff) * 64;
		int containerId = Cache.getSTORE().getIndexes()[5].getArchiveId("l" + ((absX >> 3) / 8) + "_" + ((absY >> 3) / 8));
		if (containerId == -1) {
			return;
		}
		byte[] data = Cache.getSTORE().getIndexes()[5].getFile(containerId, 0, xtea_keys);
		if (data == null) {
			failed++;
			return;
		}
		worked++;
	}

}

package org.redrune.utility.xtea;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

import org.redrune.cache.Cache;

/**
 * MapXTEAKeys.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class MapXTEAKeys {

	private final static HashMap<Integer, int[]> keys = new HashMap<Integer, int[]>();

	public static void init() {
		try {
			RandomAccessFile in = new RandomAccessFile("repository/XTEAs.mx", "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining()) {
				final int REGION_ID = buffer.getShort() & 0xffff;
				final int[] XTEAS = new int[4];
				for (int index = 0; index < 4; index++) {
					XTEAS[index] = buffer.getInt();
				}
				// if (validMapData(REGION_ID, XTEAS)) {
				keys.put(REGION_ID, XTEAS);
				// }
			}
			System.out.println("[MapXTEAKeys] " + "Loaded " + keys.size() + " xteas.");
			channel.close();
			in.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static boolean validMapData(int regionId, int[] keys) {
		int absX = (regionId >> 8) * 64;
		int absY = (regionId & 0xff) * 64;
		int containerId = Cache.getSTORE().getIndexes()[5].getArchiveId("l" + ((absX >> 3) / 8) + "_" + ((absY >> 3) / 8));
		if (containerId == -1) {
			return false;
		}
		byte[] data = Cache.getSTORE().getIndexes()[5].getFile(containerId, 0, keys);
		if (data == null) {
			return false;
		}
		return true;
	}

	public static final int[] getKeys(int regionId) {
		return keys.get(regionId);
	}

	public static HashMap<Integer, int[]> getKeys() {
		return keys;
	}
}

package org.redrune.cache.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.alex.store.Store;

/**
 *
 * @author Jonathan
 * @since Jan 31, 2014
 */
public class MapdataVerifier {

	private final static HashMap<Integer, int[]> keys = new HashMap<Integer, int[]>();

	public static void main(String[] args) throws IOException {
		Store newMaps = new Store("./data/cache/");
		// new Store("E:/Users/Jonathan/Dropbox/Sallesy 718/data/cache/");
		loadUnpackedKeys("./data/map/containersXteas/output 667/");
		// loadUnpackedKeys("E:/Users/Jonathan/Dropbox/Sallesy
		// 718/data/map/archiveKeys/unpacked/");
		int regionId = 9515;
		for (int i : keys.keySet()) {
			if (!verifyMaps(newMaps, i, getKeys(i))) {
				// if (!verifyMaps(newMaps, i, null)) {
				System.out.println("Missing map: " + i);
				// }
			}
		}
		System.out.println(verifyMaps(newMaps, regionId, getKeys(regionId)));
		// System.out.println(packOldMap(oldMaps, newMaps, regionId,
		// getKeys(regionId)));
		// for (int i : keys.keySet()) {
		// if (!verifyMaps(newMaps, i, getKeys(i))) {
		// System.out.println("Invalid xteas for region: "+i);
		/*
		 * if (verifyMaps(newMaps, i, null)) { File mapFile = new
		 * File("./data/map/archiveKeys/unpacked/" + i + ".txt"); if
		 * (mapFile.exists()) { mapFile.delete(); System.out.println(
		 * "Deleted region file: " + mapFile.getName()); } }
		 */
		// }
		// }

		// [Logger][Feb 2, 2014 1:58:20 PM]Missing xteas for region 12852.
		// [Logger][Feb 2, 2014 1:58:20 PM]Missing xteas for region 12853.
	}

	public static boolean verifyMaps(Store maps, int regionId, int[] keys) {
		int regionX = (regionId >> 8) * 64;
		int regionY = (regionId & 0xff) * 64;

		int landArchiveId = maps.getIndexes()[5].getArchiveId("l" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));
		byte[] landContainerData = maps.getIndexes()[5].getFile(landArchiveId, 0, keys);
		if (landContainerData == null && landArchiveId != -1 || (keys == null && landContainerData == null)) {
			// System.out.println("Xteas for region " + regionId + " are
			// invalid. [" + Arrays.toString(keys) + ", " + landArchiveId + ", "
			// + Arrays.toString(landContainerData) + "]");
			return false;
		} else {
			// System.out.println("Xteas for region " + regionId + " were valid!
			// [" + Arrays.toString(keys) + ", " + landArchiveId + ", " +
			// Arrays.toString(landContainerData) + "]");
			return true;
		}
	}

	public static boolean packOldMap(Store oldMaps, Store newMaps, int regionId, int[] oldKeys) {
		int regionX = (regionId >> 8) * 64;
		int regionY = (regionId & 0xff) * 64;

		int landArchiveId = oldMaps.getIndexes()[5]
				.getArchiveId("l" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));
		byte[] landContainerData = oldMaps.getIndexes()[5].getFile(landArchiveId, 0, oldKeys);
		landArchiveId = newMaps.getIndexes()[5].getArchiveId("l" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));
		return newMaps.getIndexes()[5].putFile(landArchiveId, 0, landContainerData);
	}

	public static int[] getKeys(int regionId) {
		return keys.get(regionId);
	}

	public static final void loadUnpackedKeys(String xteaPath) {
		try {
			File unpacked = new File(xteaPath);
			File[] xteasFiles = unpacked.listFiles();
			for (File region : xteasFiles) {
				String name = region.getName();
				if (!name.contains(".txt")) {
					region.delete();
					continue;
				}
				int regionId = Short.parseShort(name.replace(".txt", ""));
				if (regionId <= 0) {
					region.delete();
					continue;
				}
				BufferedReader in = new BufferedReader(new FileReader(region));
				final int[] xteas = new int[4];
				for (int index = 0; index < 4; index++) {
					xteas[index] = Integer.parseInt(in.readLine());
				}
				keys.put(regionId, xteas);
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

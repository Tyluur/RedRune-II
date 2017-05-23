package org.redrune.utility.backend;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds the mapdata XTeas.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class MapDataParser {
	
	/**
	 * The location of the folder with mapdata
	 */
	private static final String MAPDATA_FOLDER_LOCATION = "./data/repository/mapdata";
	
	/**
	 * The mapdata xteas.
	 */
	private static final Map<Integer, int[]> MAP_DATA_XTEAS = new HashMap<>();
	
	/**
	 * Initializes the mapdata.
	 */
	public static void readAll() {
		final File packedFile = new File(MAPDATA_FOLDER_LOCATION + "/packedKeys.bin");
		if (!packedFile.exists()) {
			pack();
		} else {
			load();
		}
	}
	
	/**
	 * Loads the mapdata.
	 */
	private static void load() {
		try {
			final DataInputStream in = new DataInputStream(new FileInputStream(MAPDATA_FOLDER_LOCATION + "/packedKeys.bin"));
			while (in.available() != 0) {
				final int area = in.readShort();
				final int[] parts = new int[4];
				for (int j = 0; j < 4; j++) {
					parts[j] = in.readInt();
				}
				getMapData().put(area, parts);
			}
		} catch (IOException e) {
			final File Failedpacked = new File("./data/mapdata/packedKeys.bin");
			if (Failedpacked.exists()) {
				Failedpacked.delete();
			}
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Packs the mapdata.
	 */
	private static void pack() {
		try {
			final DataOutputStream out = new DataOutputStream(new FileOutputStream(MAPDATA_FOLDER_LOCATION + "/packedKeys.bin"));
			final File unpacked = new File(MAPDATA_FOLDER_LOCATION + "/unpacked/");
			final File[] files = unpacked.listFiles();
			for (File region : files) {
				final String name = region.getName();
				if (!name.contains(".txt")) {
					continue;
				}
				final int regionId = Integer.parseInt(name.replace(".txt", ""));
				BufferedReader in = new BufferedReader(new FileReader(region));
				out.writeShort(regionId);
				final int[] Key = new int[4];
				for (int j = 0; j < 4; j++) {
					Key[j] = Integer.parseInt(in.readLine());
					out.writeInt(Key[j]);
				}
				getMapData().put(regionId, Key);
				in.close();
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			final File Failedpacked = new File(MAPDATA_FOLDER_LOCATION + "/packedKeys.bin");
			if (Failedpacked.exists()) {
				Failedpacked.delete();
			}
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Gets the mapdata xteas for the given key.
	 *
	 * @param key
	 * 		The region id.
	 * @return The mapdata xteas.
	 */
	public static int[] getMapData(int key) {
		return MAP_DATA_XTEAS.get(key);
	}
	
	/**
	 * Gets all the mapdata xteas.
	 *
	 * @return The mapdata xtea mapping.
	 */
	public static Map<Integer, int[]> getMapData() {
		return MAP_DATA_XTEAS;
	}
}

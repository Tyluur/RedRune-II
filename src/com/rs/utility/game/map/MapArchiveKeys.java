package com.rs.utility.game.map;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/30/2017
 */
public class MapArchiveKeys {
	
	/**
	 * The path to packed xteas
	 */
	private static final String PACKED_FILE_PATH = "data/repository/map/packedKeys.bin";
	
	/**
	 * The path to unpacked exteas
	 */
	private static final String UNPACKED_FILE_PATH = "data/repository/map/containersXteas/workingkeys/";
	
	/**
	 * MapKeys.
	 */
	private static Map<Integer, int[]> mapKeys = new HashMap<>();
	
	/**
	 * Initiating void.
	 */
	public static void init() {
		try {
			if (!loadPackedFile()) {
				loadUnpacked();
			}
			System.out.println("Loaded " + mapKeys.size() + " map XTEA key(s)");
		} catch (Throwable e) {
			System.err.println("Failed to load map xtea(s)!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads xteas from the packed file
	 *
	 * @return True if we could load
	 * @throws IOException
	 * 		In the case of an exception in parsing
	 */
	private static boolean loadPackedFile() throws IOException {
		File file = new File(PACKED_FILE_PATH);
		if (!file.exists()) {
			return false;
		}
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		ByteBuffer buffer = raf.getChannel().map(MapMode.READ_ONLY, 0, raf.length());
		while (buffer.remaining() > 0) {
			int id = buffer.getShort() & 0xFFFF;
			int[] key = new int[4];
			for (int i2 = 0; i2 < 4; i2++) {
				key[i2] = buffer.getInt();
			}
			mapKeys.put(id, key);
		}
		raf.close();
		return true;
	}
	
	/**
	 * Loads xteas from the unpacked file location and packs them into the file {@link #PACKED_FILE_PATH}
	 */
	private static void loadUnpacked() throws IOException {
		File directory = new File(UNPACKED_FILE_PATH);
		DataOutputStream output = new DataOutputStream(new FileOutputStream(PACKED_FILE_PATH));
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				if (file.isFile()) {
					BufferedReader input = new BufferedReader(new FileReader(file));
					int id = Integer.parseInt(file.getName().substring(0, file.getName().indexOf(".")));
					int[] keys = new int[4];
					output.writeShort(id);
					for (int i = 0; i < 4; i++) {
						String line = input.readLine();
						try {
							if (line != null) {
								keys[i] = Integer.parseInt(line);
							} else {
								System.out.println("Corrupted XTEA file : " + id);
								keys[i] = 0;
							}
						} catch (NumberFormatException e) {
							System.out.println("Corrupted XTEA file : " + id + "; line: " + line);
							keys[i] = 0;
						}
						output.writeInt(keys[i]);
					}
					input.close();
					mapKeys.put(id, keys);
				}
			}
		}
		output.close();
	}
	
	/**
	 * Gets the keys of a regionId
	 *
	 * @param regionId
	 * 		The region id
	 */
	public static int[] getKey(int regionId) {
		return mapKeys.get(regionId);
	}
	
}

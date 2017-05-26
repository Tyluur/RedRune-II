package org.redrune.cache.parse;

import org.redrune.cache.CacheContainer;
import org.redrune.cache.CacheManager;
import org.redrune.cache.stream.ByteInputStream;
import org.redrune.cache.stream.RSInputStream;
import org.redrune.rs2.node.object.GameObject;
import org.redrune.rs2.world.map.Location;
import org.redrune.rs2.world.map.region.RegionBuilder;
import org.redrune.utility.io.BufferUtils;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class MapRegionParser {
	
	private static Set<Integer> loaded = new HashSet<>();
	
	private static Set<Integer> broken = new HashSet<>();
	
	public static boolean parseMap(final int area, final int[] keys) {
		if (broken.contains(area)) {
			return false;
		}
		if (loaded.contains(area)) {
			return true;
		}
		loaded.add(area);
		int regionX = area >> 8;
		int regionY = area & 0xFF;
		try {
			byte[] landscapeMap = CacheManager.getByName(5, "m" + regionX + "_" + regionY);
			byte[] objectMap = CacheManager.getByName(5, "l" + regionX + "_" + regionY);
			if (landscapeMap == null && objectMap == null) {
				System.out.println("Map [" + (regionX << 6) + ", " + (regionY << 6) + "] was not found in the cache!");
				return true;
			}
			RSInputStream str2 = null;
			ByteInputStream str1 = null;
			if (landscapeMap != null) {
				str2 = new RSInputStream(new ByteArrayInputStream(new CacheContainer(landscapeMap).decompress()));
			}
			if (objectMap != null) {
				if (keys != null) {
					objectMap = BufferUtils.decrypt(keys, objectMap, 5, objectMap.length);
				}
				str1 = new ByteInputStream(new CacheContainer(objectMap).decompress());
			}
			int x = regionX << 6;
			int y = regionY << 6;
			byte[][][] landscapeData = new byte[4][64][64];
			if (str2 != null) {
				for (int z = 0; z < 4; z++) {
					for (int localX = 0; localX < 64; localX++) {
						for (int localY = 0; localY < 64; localY++) {
							while (true) {
								int v = str2.readByte() & 0xff;
								if (v == 0) {
									break;
								} else if (v == 1) {
									str2.readByte();
									break;
								} else if (v <= 49) {
									str2.readByte();
								} else if (v <= 81) {
									landscapeData[z][localX][localY] = (byte) (v - 49);
								}
							}
						}
					}
				}
				for (int z = 0; z < 4; z++) {
					for (int localX = 0; localX < 64; localX++) {
						for (int localY = 0; localY < 64; localY++) {
							if ((landscapeData[z][localX][localY] & 1) == 1) {
								int height = z;
								if ((landscapeData[1][localX][localY] & 2) == 2) {
									height--;
								}
								if (height >= 0 && height <= 3) {
									RegionBuilder.addClipping(x + localX, y + localY, height, 0x200000);
								}
							}
						}
					}
				}
			}
			str2.close();
			// out.writeByte(-5);//End of landscape parsing.
			if (str1 != null) {
				int objectId = -1;
				int incr;
				while ((incr = str1.readSmart2()) != 0) {
					objectId += incr;
					int location = 0;
					int incr2;
					while ((incr2 = str1.readSmart()) != 0) {
						location += incr2 - 1;
						int localX = location >> 6 & 0x3f;
						int localY = location & 0x3f;
						int height = location >> 12;
						int objectData = str1.readUByte();
						int type = objectData >> 2;
						int rotation = objectData & 0x3;
						if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
							continue;
						}
						if ((landscapeData[1][localX][localY] & 2) == 2) {
							height--;
						}
						if (height >= 0 && height <= 3) {
							RegionBuilder.addObject(new GameObject(objectId, type, rotation, Location.create(x + localX, y + localY, height)), true);
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error while loading region " + area + ", " + e.getMessage() + ", " + e.getCause() + ", " + e.toString());
			broken.add(area);
			return false;
		}
	}
	
	public static void addClipping(DataOutputStream out, int type, int x, int y, int z, int shift) {
		try {
			out.writeByte(type);
			out.writeByte(x);
			out.writeByte(y);
			out.writeByte(z);
			out.writeInt(shift);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package org.redrune.cache.parse;

import org.redrune.cache.CacheContainer;
import org.redrune.cache.CacheManager;
import org.redrune.cache.stream.ByteInputStream;
import org.redrune.cache.stream.RSInputStream;
import org.redrune.game.node.Location;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.world.region.Region;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.BufferUtils;
import org.redrune.utility.Misc;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class MapRegionParser {
	
	/**
	 * The instance of the logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(MapRegionParser.class);
	
	/**
	 * Parses the map and returns the list of objects in it
	 *
	 * @param regionId
	 * 		The id of the map
	 * @param keys
	 * 		The xtea keys of the map
	 */
	public static List<GameObject> parseMap(final int regionId, final int[] keys) {
		List<GameObject> objectList = new ArrayList<>();
		Region region = RegionManager.getRegion(regionId);
		int regionX = regionId >> 8;
		int regionY = regionId & 0xFF;
		try {
			byte[] landscapeMap = CacheManager.getByName(5, "m" + regionX + "_" + regionY);
			byte[] objectMap = CacheManager.getByName(5, "l" + regionX + "_" + regionY);
			if (landscapeMap == null && objectMap == null) {
				LOGGER.info("Map [" + (regionX << 6) + ", " + (regionY << 6) + "] was not found in the cache!");
				return objectList;
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
									region.forceGetRegionMap().addUnwalkable(height, x, y);
								}
							}
						}
					}
				}
			}
			str2.close();
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
							objectList.add(new GameObject(objectId, type, rotation, Location.create(x + localX, y + localY, height)));
						}
					}
				}
			}
			return objectList;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unable to parse region " + regionId + " with keys " + Arrays.toString(keys), e);
			RegionManager.BROKEN_REGIONS.add(regionId);
			return objectList;
		}
	}
	
}
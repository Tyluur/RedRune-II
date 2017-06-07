package org.redrune.game.world.region;

import org.redrune.game.node.Location;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class RegionDeletion {
	
	/**
	 * The map of objects that will be deleted.
	 */
	private static final Map<Integer, List<GameObject>> OBJECT_DELETE_MAP = new ConcurrentHashMap<>();
	
	/**
	 * The file that has the data of objects that will be deleted
	 */
	private static final String DELETE_FILE_LOCATION = "./data/repository/object/objects_deleted.txt";
	
	/**
	 * The instance of the logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(RegionDeletion.class);
	
	/**
	 * Prepares the {@link #OBJECT_DELETE_MAP} of objects to delete
	 */
	public static void prepare() {
		for (String line : Misc.getFileText(DELETE_FILE_LOCATION)) {
			line = line.trim();
			if (line.startsWith("/")) {
				continue;
			}
			String[] split = line.split(" ");
			Integer id = Integer.parseInt(split[0]);
			Integer x = Integer.parseInt(split[1]);
			Integer y = Integer.parseInt(split[2]);
			Integer z = Integer.parseInt(split[3]);
			Integer type = Integer.parseInt(split[4]);
			GameObject object = new GameObject(id, type, 0, Location.create(x, y, z));
			enterData(object);
		}
		LOGGER.info("Loaded " + OBJECT_DELETE_MAP.size() + " objects to delete.");
	}
	
	/**
	 * Enters the game object into the map of objects to delete
	 *
	 * @param object
	 * 		The object
	 */
	private static void enterData(GameObject object) {
		List<GameObject> objectList = OBJECT_DELETE_MAP.get(object.getId());
		if (objectList == null) {
			objectList = new ArrayList<>();
		}
		objectList.add(object);
		OBJECT_DELETE_MAP.put(object.getLocation().getRegionId(), objectList);
	}
	
	/**
	 * Gets the objects to delete in a region
	 *
	 * @param regionId
	 * 		The id of the region
	 */
	static Optional<List<GameObject>> getObjectsToDelete(int regionId) {
		final List<GameObject> value = OBJECT_DELETE_MAP.get(regionId);
		if (value == null) {
			return Optional.empty();
		}
		return Optional.of(value);
	}
}

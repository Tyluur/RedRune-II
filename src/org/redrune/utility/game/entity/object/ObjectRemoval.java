package org.redrune.utility.game.entity.object;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.WorldTile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Dec 13, 2013
 */
public class ObjectRemoval {
	
	/**
	 * The file to read from
	 */
	public static final String NONSPAWNING_OBJECTS_FILE = "data/repository/map/nonspawning.txt";
	
	/**
	 * The list of objects that aren't spawned
	 */
	private static final Set<WorldObject> OBJECTS = new LinkedHashSet<>();
	
	/**
	 * Starts up and populates the list
	 */
	public static void initialize() {
		populateList();
	}
	
	/**
	 * Populates the list with data from the file
	 */
	private static void populateList() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(NONSPAWNING_OBJECTS_FILE));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("//") || line.trim().equalsIgnoreCase("")) {
					continue;
				}
				int id = 0;
				int type = 0;
				int rotation = 0;
				int x = 0;
				int y = 0;
				int z = 0;
				try {
					String[] split = line.split(" ");
					id = Integer.parseInt(split[0]);
					type = Integer.parseInt(split[1]);
					rotation = Integer.parseInt(split[2]);
					x = Integer.parseInt(split[3]);
					y = Integer.parseInt(split[4]);
					z = Integer.parseInt(split[5]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				OBJECTS.add(new WorldObject(id, type, rotation, new WorldTile(x, y, z)));
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Loaded " + OBJECTS.size() + " objects not to be spawned.");
	}
	
	/**
	 * Gets a list of all the objects removed on a region
	 *
	 * @param regionId
	 * 		The region id
	 */
	public static List<WorldObject> getRemovedAtRegion(int regionId) {
		return OBJECTS.stream().filter(p -> p.getRegionId() == regionId).collect(Collectors.toList());
	}
	
	/**
	 * Finds out if a removed object exists on this tile, and if so we returni t
	 *
	 * @param object
	 * 		The object
	 * @return The {@code WorldObject} that existed
	 */
	public static WorldObject removedObjectExists(WorldObject object) {
		for (WorldObject loopObject : OBJECTS) {
			if (loopObject.equals(object)) {
				return object;
			}
		}
		return null;
	}
	
	public static void handleRegionChange(Player player) {
		if (!player.hasStarted()) {
			return;
		}
		OBJECTS.stream().filter(object -> object.getRegionId() == player.getRegionId()).forEach(object -> {
			String key = "destroyed_object_" + object.getId() + "_" + object.getRegionId();
			if (player.getTemporaryAttribute(key, false)) {
				return;
			}
			player.getPackets().sendDestroyObject(object);
			player.putAttribute(key, true);
		});
	}
	
	
}

package org.redrune.utility.game.repository.object.climbable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.redrune.cache.Cache;
import org.redrune.cache.loaders.ObjectDefinitions;
import org.redrune.game.content.entity.object.ClimbActionHandler;
import org.redrune.utility.file.JsonFileManager;
import org.redrune.utility.functions.Misc;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-21
 */
public class ClimbableObjectRepository {
	
	/**
	 * The doors mapping.
	 */
	private static final Map<Integer, ClimbableObject> CLIMBABLE_OBJECTS = new HashMap<>();
	
	/**
	 * The location of the configuration file
	 */
	private static final String CONFIGURATION_FILE = "./data/repository/object/climbable_objects.json";
	
	/**
	 * The gson instance
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static void main(String[] args) throws IOException {
		Cache.initialize();
		List<ClimbableObject> objects = new ArrayList<>();
		for (int i = 0; i < Misc.getObjectDefinitionsSize(); i++) {
			ObjectDefinitions def = ObjectDefinitions.getObjectDefinitions(i);
			if (def == null) {
				System.out.println("Unable to object definitions for object " + i);
				continue;
			}
			if (ClimbActionHandler.isLadder(def)) {
				objects.add(new ClimbableObject(i, def.getName()));
			}
		}
		saveClimbables(objects);
	}
	
	/**
	 * Loads all climbable objects from the file
	 */
	public static void initialize() {
		List<ClimbableObject> climbableObjects = getListFromFile();
		if (climbableObjects == null) {
			throw new IllegalStateException("Unable to parse doors from file {" + CONFIGURATION_FILE + "}, recheck running directory!");
		}
		for (ClimbableObject object : climbableObjects) {
			CLIMBABLE_OBJECTS.put(object.getObjectId(), object);
		}
		System.out.println("Loaded " + CLIMBABLE_OBJECTS.size() + " climbable objects");
		
	}
	
	/**
	 * Gets the ids of all the climbable objects that have been registered
	 */
	public static Set<Integer> getObjectIds() {
		return CLIMBABLE_OBJECTS.keySet();
	}
	
	/**
	 * Loads all the data for doors from the file
	 */
	private static List<ClimbableObject> getListFromFile() {
		File file = new File(CONFIGURATION_FILE);
		if (!file.exists()) {
			return null;
		}
		String text = Misc.getText(CONFIGURATION_FILE);
		return GSON.fromJson(text, new TypeToken<List<ClimbableObject>>() {
		}.getType());
	}
	
	/**
	 * Saves the list of doors to file
	 *
	 * @param doors
	 * 		The list of doors to save
	 */
	private static void saveClimbables(List<ClimbableObject> doors) {
		JsonFileManager.save(doors, CONFIGURATION_FILE);
	}
}

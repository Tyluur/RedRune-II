package org.redrune.utility.repository.npc.spawn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.redrune.game.world.World;
import org.redrune.utility.Misc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/30/2017
 */
public class NPCSpawnRepository {
	
	/**
	 * The gson instance
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * The location that data will be stored
	 */
	private static final String DATA_LOCATION = "./data/repository/npc/regions/";
	
	/**
	 * Loads all of the {@link NPCSpawn}s of the region into the world
	 *
	 * @param regionId
	 * 		The region to find the spawns of
	 */
	public static void loadSpawns(int regionId) {
		if (!regionSpawnsExist(regionId)) {
			return;
		}
		List<NPCSpawn> spawns = loadFromFile(regionId);
		if (spawns == null) {
			return;
		}
		spawns.forEach(World.get()::addSpawn);
	}
	
	/**
	 * Checks if there are spawns for the region
	 *
	 * @param regionId
	 * 		The region id to check for
	 */
	private static boolean regionSpawnsExist(int regionId) {
		return new File(getFileLocation(regionId)).exists();
	}
	
	/**
	 * Loads and constructs a new {@code NPCSpawn} {@code List} from the {@link #getFileLocation(int)} for the region
	 * id
	 *
	 * @param regionId
	 * 		The id of the reigon
	 */
	private static List<NPCSpawn> loadFromFile(int regionId) {
		File file = new File(getFileLocation(regionId));
		if (!file.exists()) {
			return null;
		}
		return GSON.fromJson(Misc.getText(file.getAbsolutePath()), new TypeToken<List<NPCSpawn>>() {
		}.getType());
	}
	
	/**
	 * @param regionId
	 * 		The id of the region
	 */
	private static String getFileLocation(int regionId) {
		return DATA_LOCATION + regionId + ".json";
	}
	
	/**
	 * Saves the data to a file
	 *
	 * @param regionId
	 * 		The region of the spawns
	 * @param spawns
	 * 		The spawn data to write
	 */
	public static void saveData(int regionId, List<NPCSpawn> spawns) {
		try (Writer writer = new FileWriter(getFileLocation(regionId))) {
			GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
			Gson gson = builder.create();
			gson.toJson(spawns, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

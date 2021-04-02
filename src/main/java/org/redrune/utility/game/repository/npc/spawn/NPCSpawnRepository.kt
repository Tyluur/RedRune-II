package org.redrune.utility.game.repository.npc.spawn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.global.World;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.functions.Misc.FaceDirection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
public class NPCSpawnRepository {
	
	/**
	 * The gson instance
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * The location that the spawns will be stored
	 */
	private static final String SPAWNS_LOCATION = "./data/repository/npc/spawns/";
	
	/**
	 * Adds a spawn to the list of spawns and saves it
	 *
	 * @param npcId
	 * 		The id of the spawn
	 * @param tile
	 * 		The tile of the spawn
	 * @param direction
	 * 		The direction of the spawn
	 */
	public static void addSpawn(int npcId, WorldTile tile, FaceDirection direction) {
		List<NPCSpawn> spawns = loadFromFile(tile.getRegionId());
		if (spawns == null) {
			spawns = new ArrayList<>();
		}
		spawns.add(new NPCSpawn(npcId, tile, direction));
		saveData(tile.getRegionId(), spawns);
		World.spawnNPC(npcId, tile, -1, true, direction);
		System.out.println("Spawned " + npcId + " on " + tile + " facing " + direction + " at region " + tile.getRegionId());
	}
	
	/**
	 * Removes an npc spawn
	 *
	 * @param npc
	 * 		The npc to remove the spawn for
	 */
	public static void removeSpawn(NPC npc) {
		List<NPCSpawn> spawns = loadFromFile(npc.getRegionId());
		if (spawns == null) {
			return;
		}
		boolean removed = false;
		Iterator<NPCSpawn> it$ = spawns.iterator();
		while (it$.hasNext()) {
			NPCSpawn spawn = it$.next();
			if (spawn.getNpcId() == npc.getId() && spawn.getTile().matches(npc.getRespawnTile())) {
				it$.remove();
				removed = true;
			}
		}
		if (removed) {
			saveData(npc.getRegionId(), spawns);
			System.out.println("Removed npc and saved file!\t" + npc);
		}
	}
	
	/**
	 * Loads all of the {@link NPCSpawn}s of the region into the world
	 *
	 * @param regionId
	 * 		The region to find the spawns of
	 */
	public static void loadSpawns(int regionId) {
		if (!regionSpawnsExist(regionId)) {
			System.out.println("region spawn " + regionId + " does not exist");
			return;
		}
		List<NPCSpawn> spawns = loadFromFile(regionId);
		if (spawns == null) {
			System.out.println("region spawn " + regionId + " does not exist");
			return;
		}
		spawns.forEach(spawn -> World.spawnNPC(spawn.getNpcId(), spawn.getTile(), -1, true, spawn.getDirection()));
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
	
	/**
	 * Loads and constructs a new {@code NPCSpawn} {@code List} from the {@link #getFileLocation(int)} for the region
	 * id
	 *
	 * @param regionId
	 * 		The id of the region
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
	 * Checks if there are spawns for the region
	 *
	 * @param regionId
	 * 		The region id to check for
	 */
	private static boolean regionSpawnsExist(int regionId) {
		return new File(getFileLocation(regionId)).exists();
	}
	
	/**
	 * @param regionId
	 * 		The id of the region
	 */
	private static String getFileLocation(int regionId) {
		return SPAWNS_LOCATION + regionId + ".json";
	}
	
}

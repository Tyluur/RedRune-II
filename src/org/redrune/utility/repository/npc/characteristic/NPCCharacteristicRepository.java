package org.redrune.utility.repository.npc.characteristic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.npc.data.NPCCharacteristics;
import org.redrune.game.node.entity.npc.data.NPCCombatDefinitions;
import org.redrune.utility.tool.Misc;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/21/2017
 */
public class NPCCharacteristicRepository {
	
	/**
	 * The gson instance
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * The location of the folder that has all npc characteristics
	 */
	private static final String CHARACTERISTIC_FOLDER_LOCATION = "./data/repository/npc/characteristics/";
	
	/**
	 * The map of characteristics
	 */
	private static final Map<Integer, NPCCharacteristics> CHARACTERISTICS_MAP = new ConcurrentHashMap<>();
	
	/**
	 * Gets the {@code NPCCharacteristics} of an {@code NPC} using caching to reduce speed
	 *
	 * @param npc
	 * 		The npc
	 */
	public static NPCCharacteristics getCharacteristics(NPC npc) {
		// the id of the npc
		int npcId = npc.getId();
		// using caching with the map
		if (CHARACTERISTICS_MAP.containsKey(npcId)) {
			System.out.println("we returned an already loaded characteristic for " + npc);
			return CHARACTERISTICS_MAP.get(npcId);
		} else {
			// we first check it from the file
			NPCCharacteristics characteristics = loadFileCharacteristics(npc);
			// if we couldn't load anything from the file, we will create a blank characteristics file
			// this makes it so we avoid loading another copy of the file
			if (characteristics == null) {
				characteristics = new NPCCharacteristics();
				System.out.println("unable to load characteristics from file for " + npc);
			} else {
				System.out.println("loaded characteristics from file for " + npc);
			}
			// we have no bonuses set, so we generate all 0 default bonuses [ensures we always generate bonuses]
			if (!characteristics.getBonuses().containsKey(npcId)) {
				characteristics.getBonuses().put(npcId, findFirstBonuses(characteristics.getBonuses()).orElse(new int[10]));
			}
			// we have no combat definitions, so we will store default ones for this npc [ensures we always generate bonuses]
			if (!characteristics.getCombatDefinitions().containsKey(npcId)) {
				characteristics.getCombatDefinitions().put(npcId, findBestDefinition(characteristics.getCombatDefinitions()).orElse(new NPCCombatDefinitions()));
			}
			// cache the data
			CHARACTERISTICS_MAP.put(npcId, characteristics);
			return characteristics;
		}
	}
	
	/**
	 * Finds the first definition in the map, this is only used to set definitions of an npc who doesn't have any
	 *
	 * @param map
	 * 		The map
	 */
	private static Optional<NPCCombatDefinitions> findBestDefinition(Map<Integer, NPCCombatDefinitions> map) {
		for (NPCCombatDefinitions def : map.values()) {
			if (def != null) {
				return Optional.of(def);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Finds the first bonus in the map, this is only used to set definitions of an npc who doesn't have any
	 *
	 * @param map
	 * 		The map
	 */
	private static Optional<int[]> findFirstBonuses(Map<Integer, int[]> map) {
		for (int[] def : map.values()) {
			if (def != null) {
				return Optional.of(def);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Constructs a {@code NPCCharacteristics} object from the npc's characteristics file.
	 *
	 * @param npc
	 * 		The npc
	 */
	private static NPCCharacteristics loadFileCharacteristics(NPC npc) {
		// the name of the npc [used to find the file]
		String name = npc.getDefinitions().getName();
		// create the file
		File file = new File(getFileLocation(name));
		// if the file is not present, we can't load characteristics
		if (!file.exists()) {
			return null;
		}
		// get the text that is in the json file
		String text = Misc.getText(file.getAbsolutePath());
		// constructs the file from the serialized json file
		return GSON.fromJson(text, NPCCharacteristics.class);
	}
	
	/**
	 * Gets the location that the characteristics of the file should be in.
	 *
	 * @param name
	 * 		The name of the npc
	 */
	private static String getFileLocation(String name) {
		return CHARACTERISTIC_FOLDER_LOCATION + name + ".json";
	}
	
}

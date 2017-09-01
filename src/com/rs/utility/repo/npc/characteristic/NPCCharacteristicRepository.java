package com.rs.utility.repo.npc.characteristic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.entity.actor.npc.Drop;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.utility.Misc;
import com.rs.utility.constants.NPCConstants;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class NPCCharacteristicRepository {
	
	/**
	 * The map of cached characteristics
	 */
	private static final Map<String, NPCCharacteristic> CACHED_CHARACTERISTICS = new HashMap<>();
	
	/**
	 * The default npc definition
	 */
	private final static NPCCombatDefinitions DEFAULT_DEFINITION = new NPCCombatDefinitions(1, -1, -1, -1, 5, 1, 33, 0, NPCConstants.MELEE, -1, -1, NPCConstants.PASSIVE);
	
	/**
	 * The gson instance
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * The location of all npc characteristics
	 */
	private static final String CHARACTERISTICS_LOCATION = "./data/repository/npc/characteristics/";
	
	/**
	 * Gets the {@code NPCCharacteristic} instance from the file
	 *
	 * @param name
	 * 		The name of the npc we will use to find the file
	 */
	private static NPCCharacteristic getCharacteristicsFromFile(String name) {
		String fileLocation = getFileLocation(name);
		File file = new File(fileLocation);
		if (!file.exists()) {
			return null;
		}
		String text = Misc.getText(fileLocation);
		return GSON.fromJson(text, NPCCharacteristic.class);
	}
	
	/**
	 * @param name
	 * 		The name of the npc
	 */
	private static String getFileLocation(String name) {
		return CHARACTERISTICS_LOCATION + name + ".json";
	}
	
	public static void convertNPCCombatDefinitions() {
		/*HashMap<Integer, NPCCombatDefinitions> defs = NPCCombatDefinitionsL.getNpcCombatDefinitions();
		System.out.println("Defs=" + defs.size());
		for (Entry<Integer, NPCCombatDefinitions> entry : defs.entrySet()) {
			int npcId = entry.getKey();
			NPCCombatDefinitions definitions = entry.getValue();
			NPCDefinitions npcDefinitions = NPCDefinitions.getNPCDefinitions(npcId);
			if (npcDefinitions == null) {
				System.out.println("No definitions for #" + npcId + "");
				continue;
			}
			String name = npcDefinitions.getName();
			NPCCharacteristic characteristic = getCharacteristicsFromFile(name);
			if (characteristic == null) {
				characteristic = new NPCCharacteristic();
			}
			characteristic.addCombatDefinitions(npcId, definitions);
			Misc.saveToJsonFile(getFileLocation(name), characteristic);
			System.out.println("Saved [" + name + "][" + characteristic + "]");
		}*/
	}
	
	public static void convertNPCDrops() {
		/*HashMap<Integer, Drop[]> drops = NPCDrops.getDropMap();
		System.out.println("drops=" + drops.size());
		for (Entry<Integer, Drop[]> entry : drops.entrySet()) {
			int npcId = entry.getKey();
			Drop[] dropArray = entry.getValue();
			List<Drop> dropList = Arrays.asList(dropArray);
			NPCDefinitions npcDefinitions = NPCDefinitions.getNPCDefinitions(npcId);
			if (npcDefinitions == null) {
				System.out.println("No definitions for #" + npcId + "");
				continue;
			}
			String name = npcDefinitions.getName();
			NPCCharacteristic characteristic = getCharacteristicsFromFile(name);
			if (characteristic == null) {
				characteristic = new NPCCharacteristic();
			}
			characteristic.addDrops(npcId, dropList);
			Misc.saveToJsonFile(getFileLocation(name), characteristic);
			System.out.println("Saved [" + name + "][" + characteristic + "]");
		}*/
	}
	
	public static void convertNPCBonuses() {
		/*HashMap<Integer, int[]> drops = NPCBonuses.getNpcBonuses();
		System.out.println("drops=" + drops.size());
		for (Entry<Integer, int[]> entry : drops.entrySet()) {
			int npcId = entry.getKey();
			int[] bonuses = entry.getValue();
			NPCDefinitions npcDefinitions = NPCDefinitions.getNPCDefinitions(npcId);
			if (npcDefinitions == null) {
				System.out.println("No definitions for #" + npcId + "");
				continue;
			}
			String name = npcDefinitions.getName();
			NPCCharacteristic characteristic = getCharacteristicsFromFile(name);
			if (characteristic == null) {
				characteristic = new NPCCharacteristic();
			}
			characteristic.addBonuses(npcId, bonuses);
			Misc.saveToJsonFile(getFileLocation(name), characteristic);
			System.out.println("Saved [" + name + "][" + characteristic + "]");
		}*/
	}
	
	public static void convertNPCExamines() {
		/*Map<Integer, String> drops = NPCExamines.getEXAMINES();
		System.out.println("drops=" + drops.size());
		for (Entry<Integer, String> entry : drops.entrySet()) {
			int npcId = entry.getKey();
			String examine = entry.getValue();
			NPCDefinitions npcDefinitions = NPCDefinitions.getNPCDefinitions(npcId);
			if (npcDefinitions == null) {
				System.out.println("No definitions for #" + npcId + "");
				continue;
			}
			String name = npcDefinitions.getName();
			NPCCharacteristic characteristic = getCharacteristicsFromFile(name);
			if (characteristic == null) {
				characteristic = new NPCCharacteristic();
			}
			characteristic.addExamine(npcId, examine);
			Misc.saveToJsonFile(getFileLocation(name), characteristic);
			System.out.println("Saved [" + name + "][" + characteristic + "]");
		}*/
	}
	
	/**
	 * Gets the drops of an npc by its id
	 */
	public static List<Drop> getDrops(int npcId) {
		NPCCharacteristic characteristic = getCharacteristics(npcId);
		if (characteristic == null) {
			return null;
		}
		return characteristic.getDrops(npcId);
	}
	
	/**
	 * Gets the characteristics of an npc by its id
	 *
	 * @param npcId
	 * 		The id of the npc
	 */
	private static NPCCharacteristic getCharacteristics(int npcId) {
		NPCDefinitions definitions = NPCDefinitions.getNPCDefinitions(npcId);
		if (definitions == null) {
			System.out.println("Unable to find definitions for npc " + npcId);
			return null;
		}
		String name = definitions.getName();
		NPCCharacteristic cached = CACHED_CHARACTERISTICS.get(name);
		if (!CACHED_CHARACTERISTICS.containsKey(name)) {
			cached = getCharacteristicsFromFile(name);
			if (cached == null) {
				CACHED_CHARACTERISTICS.put(name, null);
				return null;
			}
			CACHED_CHARACTERISTICS.put(name, cached);
			return cached;
		} else {
			return cached;
		}
	}
	
	/**
	 * Gets the combat definitions of an npc by its id
	 */
	public static NPCCombatDefinitions getCombatDefinitions(int npcId) {
		NPCCharacteristic characteristic = getCharacteristics(npcId);
		if (characteristic == null) {
			return DEFAULT_DEFINITION;
		}
		NPCCombatDefinitions combatDefinitions = characteristic.getCombatDefinitions(npcId);
		if (combatDefinitions == null) {
			return DEFAULT_DEFINITION;
		}
		return combatDefinitions;
	}
	
	/**
	 * Gets the bonuses of an npc by its id
	 */
	public static int[] getBonuses(int npcId) {
		NPCCharacteristic characteristic = getCharacteristics(npcId);
		if (characteristic == null) {
			return null;
		}
		return characteristic.getBonuses(npcId);
	}
	
	/**
	 * Gets the examine of an npc by its id
	 */
	public static String getExamine(int npcId) {
		NPCCharacteristic characteristic = getCharacteristics(npcId);
		if (characteristic == null) {
			return "It's an npc.";
		}
		String examine = characteristic.getExamine(npcId);
		if (examine == null) {
			return "It's an npc.";
		}
		return examine;
	}
	
}

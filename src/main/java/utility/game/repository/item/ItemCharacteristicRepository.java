package utility.game.repository.item;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cache.codec.loaders.ItemDefinitions;
import game.entity.item.Item;
import utility.functions.Misc;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
public class ItemCharacteristicRepository {
	
	/**
	 * The map of cached item characteristics
	 */
	private static final Map<String, ItemCharacteristic> CHARACTERISTIC_CACHE = new HashMap<>();
	
	/**
	 * The gson instance
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * The location of all item characteristics
	 */
	private static final String CHARACTERISTICS_LOCATION = "./data/repository/item/characteristics/";
	
	public static void convertBonuses() {
		/*HashMap<Integer, int[]> defs = ItemBonuses.getItemBonuses();
		System.out.println("Defs=" + defs.size());
		for (Entry<Integer, int[]> entry : defs.entrySet()) {
			int itemId = entry.getKey();
			int[] bonuses = entry.getValue();
			ItemDefinitions definitions = ItemDefinitions.getItemDefinitions(itemId);
			if (definitions == null) {
				System.out.println("No definitions for #" + itemId + "");
				continue;
			}
			String name = definitions.getName();
			ItemCharacteristic characteristic = getCharacteristicsFromFile(name);
			if (characteristic == null) {
				characteristic = new ItemCharacteristic();
			}
			characteristic.addBonuses(itemId, bonuses);
			Misc.saveToJsonFile(getFileLocation(name), characteristic);
		}
		System.out.println("finished bonuses");*/
	}
	
	public static void convertExamines() {
	/*	HashMap<Integer, String> defs = ItemExamines.getItemExamines();
		System.out.println("Defs=" + defs.size());
		for (Entry<Integer, String> entry : defs.entrySet()) {
			int itemId = entry.getKey();
			String examine = entry.getValue();
			ItemDefinitions definitions = ItemDefinitions.getItemDefinitions(itemId);
			if (definitions == null) {
				System.out.println("No definitions for #" + itemId + "");
				continue;
			}
			String name = definitions.getName();
			ItemCharacteristic characteristic = getCharacteristicsFromFile(name);
			if (characteristic == null) {
				characteristic = new ItemCharacteristic();
			}
			characteristic.addExamine(itemId, examine);
			Misc.saveToJsonFile(getFileLocation(name), characteristic);
		}
		System.out.println("finished examines");*/
	}
	
	/**
	 * Gets the characteristic instance from a file
	 */
	private static ItemCharacteristic getCharacteristicsFromFile(String name) {
		String fileLocation = getFileLocation(name);
		File file = new File(fileLocation);
		if (!file.exists()) {
			return null;
		}
		String text = Misc.getText(fileLocation);
		return GSON.fromJson(text, ItemCharacteristic.class);
	}
	
	/**
	 * @param name
	 * 		The name of the item
	 */
	private static String getFileLocation(String name) {
		return CHARACTERISTICS_LOCATION + getProperName(name) + ".json";
	}
	
	/**
	 * Gets the proper name of an item
	 *
	 * @param name
	 * 		The name
	 */
	private static String getProperName(String name) {
		return name.replace("/", "_").replace("\\", "_");
	}
	
	/**
	 * Gets the bonuses of an item by its id
	 */
	public static int[] getBonuses(int itemId) {
		ItemCharacteristic characteristic = getCharacteristics(itemId);
		if (characteristic == null) {
			return null;
		}
		return characteristic.getBonuses(itemId);
	}
	/**
	 * Gets the examine of an item by its id
	 */
	public static String getExamine(Item item) {
		return item == null ? "It's an item" : getExamine(item.getId());
	}
	
	/**
	 * Gets the examine of an item by its id
	 */
	public static String getExamine(int itemId) {
		ItemCharacteristic characteristic = getCharacteristics(itemId);
		if (characteristic == null) {
			return "It's an item";
		}
		return characteristic.getExamine(itemId);
	}
	
	/**
	 * Gets the characteristics of an item by its id
	 */
	private static ItemCharacteristic getCharacteristics(int itemId) {
		ItemDefinitions definitions = ItemDefinitions.getItemDefinitions(itemId);
		if (definitions == null) {
			System.out.println("Unable to find definitions for item " + itemId);
			return null;
		}
		String name = getProperName(definitions.getName());
		ItemCharacteristic cached = CHARACTERISTIC_CACHE.get(name);
		if (!CHARACTERISTIC_CACHE.containsKey(name)) {
			cached = getCharacteristicsFromFile(name);
			if (cached == null) {
				CHARACTERISTIC_CACHE.put(name, null);
				return null;
			}
			CHARACTERISTIC_CACHE.put(name, cached);
			return cached;
		} else {
			return cached;
		}
	}
}

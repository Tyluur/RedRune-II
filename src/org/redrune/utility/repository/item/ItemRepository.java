package org.redrune.utility.repository.item;

import com.google.gson.reflect.TypeToken;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.cache.parse.definition.ItemDefinition;
import org.redrune.game.node.item.Item;
import org.redrune.utility.Misc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public final class ItemRepository {
	
	/**
	 * The location of the folder with item data
	 */
	private static final String ITEM_REPOSITORY_LOCATION = "./data/repository/item/data/";
	
	/**
	 * The list of items by name that are untradeable
	 */
	private static final List<String> UNTRADEABLES = new ArrayList<>();
	
	/**
	 * The untradeable cache.
	 */
	private static final Map<Integer, Boolean> UNTRADEABLE_CACHE = new HashMap<>();
	
	/**
	 * The map of all the data
	 */
	private static final Map<Integer, ItemData> DATA_MAP = new ConcurrentHashMap<>();
	
	/**
	 * The logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(ItemRepository.class);
	
	/**
	 * Loads all untradeable items.
	 */
	public static void loadUntradeables() {
		UNTRADEABLES.clear();
		List<String> fileText = Misc.getFileText("./data/resource/items/nontradeables.txt");
		UNTRADEABLES.addAll(fileText);
		LOGGER.info("Loaded " + UNTRADEABLES.size() + " untradeable items.");
	}
	
	/**
	 * Finds the bonuses of an item
	 *
	 * @param itemId
	 * 		The item
	 */
	public static int[] getBonuses(int itemId) {
		ItemData data = getItemData(itemId);
		return data == null ? null : data.getBonuses();
	}
	
	/**
	 * Gets the examine of an item
	 *
	 * @param itemId
	 * 		The id of the item
	 */
	public static String getExamine(int itemId) {
		ItemData data = getItemData(itemId);
		return data == null ? null : data.getExamine();
	}
	
	/**
	 * Gets the item data using caching to increase efficiency
	 *
	 * @param itemId
	 * 		The id of the item
	 */
	private static ItemData getItemData(int itemId) {
		ItemData data = DATA_MAP.get(itemId);
		boolean add = false;
		if (data == null) {
			data = loadFileData(itemId);
			add = true;
		}
		if (add) {
			DATA_MAP.put(itemId, data);
		}
		return data;
	}
	
	/**
	 * Loads the file data
	 *
	 * @param itemId
	 * 		The id of the item
	 */
	private static ItemData loadFileData(int itemId) {
		File file = new File(ITEM_REPOSITORY_LOCATION + itemId + ".json");
		if (!file.exists()) {
			return null;
		}
		return Misc.getGSON().fromJson(Misc.getText(file.getAbsolutePath()), new TypeToken<ItemData>() {
		}.getType());
	}
	
	/**
	 * Checks if an item is untradeable by parsing through the list of {@link #UNTRADEABLES} and checking for the item's
	 * name, or id. This uses caching in order to skip looping through a large list every time.
	 *
	 * @param item
	 * 		The item
	 */
	public static boolean isUntradeable(int itemId) {
		Boolean untradeable = UNTRADEABLE_CACHE.get(itemId);
		if (untradeable == null) {
			ItemDefinition definitions = ItemDefinitionParser.forId(itemId);
			boolean flagged = false;
			for (String listName : UNTRADEABLES) {
				if (Misc.isNumeric(listName)) {
					int id = Integer.parseInt(listName);
					if (itemId == id) {
						flagged = true;
						break;
					}
				} else {
					if (definitions.getName().equalsIgnoreCase(listName)) {
						flagged = true;
						break;
					}
				}
			}
			UNTRADEABLE_CACHE.put(itemId, flagged);
			return false;
		} else {
			return untradeable;
		}
	}
	
}

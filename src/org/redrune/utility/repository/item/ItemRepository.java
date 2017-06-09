package org.redrune.utility.repository.item;

import com.google.gson.reflect.TypeToken;
import org.redrune.utility.Misc;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
	 * The map of all the data
	 */
	private static final Map<Integer, ItemData> DATA_MAP = new ConcurrentHashMap<>();
	
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
	
}

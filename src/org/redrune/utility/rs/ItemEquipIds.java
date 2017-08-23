package org.redrune.utility.rs;

import org.redrune.utility.tool.Misc;

import java.util.HashMap;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/22/2017
 */
public final class ItemEquipIds {
	
	/**
	 * The map of equip ids
	 */
	private static final HashMap<Integer, Integer> EQUIP_ID_MAP = new HashMap<>();
	
	private ItemEquipIds() {
	
	}
	
	/**
	 * Loads all equip ids
	 */
	public static void registerAll() {
		List<String> lines = Misc.getFileText("./data/repository/item/equipids.txt");
		lines.forEach(line -> {
			String[] split = line.split("=");
			EQUIP_ID_MAP.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		});
	}
	
	/**
	 * Gets an equip id from the map for an item
	 *
	 * @param itemId
	 * 		The id of the item
	 */
	public static int getEquipId(int itemId) {
		Integer equipId = EQUIP_ID_MAP.get(itemId);
		if (equipId == null) {
			return -1;
		}
		return equipId;
	}
}


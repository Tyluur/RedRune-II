package org.redrune.cache.parse;

import java.util.concurrent.ConcurrentHashMap;

import org.redrune.cache.Cache;
import org.redrune.cache.parse.definition.ItemDefinition;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class ItemDefinitionParser {
	
	/**
	 * The map of definitions
	 */
	private static final ConcurrentHashMap<Integer, ItemDefinition> ITEM_DEFINITIONS = new ConcurrentHashMap<>();
	
	/**
	 * Gets the definitions of an item by the id
	 *
	 * @param itemId
	 * 		The id of the item
	 * @return An {@code ItemDefinition} {@code Object}
	 */
	public static ItemDefinition forId(int itemId) {
		ItemDefinition definitions = ITEM_DEFINITIONS.get(itemId);
		if (definitions != null) {
			return definitions;
		} else {
			ItemDefinition def = new ItemDefinition(itemId);
			def.loadItemDefinition();
			ITEM_DEFINITIONS.put(itemId, def);
			return def;
		}
	}
	
	/**
	 * Loads all equip ids
	 */
	public static void loadEquipIds() {
		int equipId = 0;
		for (int i = 0; i < Cache.getAmountOfItems(); i++) {
			ItemDefinition def = forId(i);
			if (def.getMaleWornModelId1() >= 0 || def.getMaleWornModelId2() >= 0) {
				def.setEquipId(equipId++);
			}
		}
	}
	
}
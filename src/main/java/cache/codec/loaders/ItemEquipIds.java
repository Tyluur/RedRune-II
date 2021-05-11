package cache.codec.loaders;

import utility.functions.Misc;

import java.util.HashMap;

public final class ItemEquipIds {
	
	private static final HashMap<Integer, Integer> itemsDefinitions = new HashMap<>();
	
	private ItemEquipIds() {
	
	}
	
	public static void initialize() {
		int equipId = 0;
		for (int itemId = 0; itemId < Misc.getItemDefinitionsSize(); itemId++) {
			ItemDefinitions itemDefinitions = ItemDefinitions.getItemDefinitions(itemId);
			if (itemDefinitions.getMaleWornModelId1() >= 0 || itemDefinitions.getFemaleWornModelId1() >= 0) {
				itemsDefinitions.put(itemId, equipId++);
			}
		}
	}
	
	public static int getEquipId(int itemId) {
		Integer equipId = itemsDefinitions.get(itemId);
		if (equipId == null) {
			return -1;
		}
		return equipId;
		
	}
}

package utility.game.repository.item;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
public final class ItemCharacteristic {
	
	/**
	 * The map of bonuses
	 */
	private final Map<Integer, int[]> bonusMap = new HashMap<>();
	
	/**
	 * The map of examines
	 */
	private final Map<Integer, String> examineMap = new HashMap<>();
	
	/**
	 * Adds the bonuses of an item
	 */
	public void addBonuses(int itemId, int[] bonuses) {
		bonusMap.put(itemId, bonuses);
	}
	
	/**
	 * Gets the bonuses of an item
	 */
	public int[] getBonuses(int itemId) {
		return bonusMap.get(itemId);
	}
	
	/**
	 * Adds the examine of an item
	 */
	public void addExamine(int itemId, String examine) {
		examineMap.put(itemId, examine);
	}
	
	/**
	 * Gets the examine of an item
	 */
	public String getExamine(int itemId) {
		return examineMap.get(itemId);
	}
	
	@Override
	public String toString() {
		return "ItemCharacteristic{" + "bonusMap=" + bonusMap.size() + ", examineMap=" + examineMap.size() + '}';
	}
}

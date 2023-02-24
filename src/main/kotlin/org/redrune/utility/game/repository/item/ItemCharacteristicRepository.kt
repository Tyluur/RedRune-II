package org.redrune.utility.game.repository.item

import com.google.gson.GsonBuilder
import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.game.entity.item.Item
import org.redrune.utility.functions.Misc
import java.io.File

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/1/2017
 */
object ItemCharacteristicRepository {
    /**
     * The map of cached item characteristics
     */
    private val CHARACTERISTIC_CACHE: MutableMap<String, ItemCharacteristic?> = HashMap()

    /**
     * The gson instance
     */
    private val GSON = GsonBuilder().setPrettyPrinting().create()

    /**
     * The location of all item characteristics
     */
    private const val CHARACTERISTICS_LOCATION = "./data/repository/item/characteristics/"

    fun convertBonuses() {
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

    fun convertExamines() {
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
    private fun getCharacteristicsFromFile(name: String): ItemCharacteristic? {
        val fileLocation = getFileLocation(name)
        val file = File(fileLocation)
        if (!file.exists()) {
            return null
        }
        val text = Misc.getText(fileLocation)
        return GSON.fromJson(text, ItemCharacteristic::class.java)
    }

    /**
     * @param name The name of the item
     */
    private fun getFileLocation(name: String): String {
        return CHARACTERISTICS_LOCATION + getProperName(name) + ".json"
    }

    /**
     * Gets the proper name of an item
     *
     * @param name The name
     */
    private fun getProperName(name: String): String {
        return name.replace("/", "_").replace("\\", "_")
    }

    /**
     * Gets the bonuses of an item by its id
     */
    @JvmStatic
    fun getBonuses(itemId: Int): IntArray? {
        val characteristic = getCharacteristics(itemId) ?: return null
        return characteristic.getBonuses(itemId)
    }

    /**
     * Gets the examine of an item by its id
     */
    @JvmStatic
    fun getExamine(item: Item?): String {
        return if (item == null) "It's an item" else getExamine(item.id)!!
    }

    /**
     * Gets the examine of an item by its id
     */
    @JvmStatic
    fun getExamine(itemId: Int): String? {
        val characteristic = getCharacteristics(itemId) ?: return "It's an item"
        return characteristic.getExamine(itemId)
    }

    /**
     * Gets the characteristics of an item by its id
     */
    private fun getCharacteristics(itemId: Int): ItemCharacteristic? {
        val definitions = ItemDefinitions.getItemDefinitions(itemId)
        if (definitions == null) {
            println("Unable to find definitions for item $itemId")
            return null
        }
        val name = getProperName(definitions.name)
        var cached = CHARACTERISTIC_CACHE[name]
        return if (!CHARACTERISTIC_CACHE.containsKey(name)) {
            cached = getCharacteristicsFromFile(name)
            if (cached == null) {
                CHARACTERISTIC_CACHE[name] = null
                return null
            }
            CHARACTERISTIC_CACHE[name] = cached
            cached
        } else {
            cached
        }
    }
}
package org.redrune.utility.game.repository.item

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
class ItemCharacteristic {

    /**
     * The map of bonuses
     */
    private val bonusMap: MutableMap<Int, IntArray> = HashMap()

    /**
     * The map of examines
     */
    private val examineMap: MutableMap<Int, String> = HashMap()

    /**
     * Adds the bonuses of an item
     */
    fun addBonuses(itemId: Int, bonuses: IntArray) {
        bonusMap[itemId] = bonuses
    }

    /**
     * Gets the bonuses of an item
     */
    fun getBonuses(itemId: Int): IntArray? {
        return bonusMap[itemId]
    }

    /**
     * Adds the examine of an item
     */
    fun addExamine(itemId: Int, examine: String) {
        examineMap[itemId] = examine
    }

    /**
     * Gets the examine of an item
     */
    fun getExamine(itemId: Int): String? {
        return examineMap[itemId]
    }

    override fun toString(): String {
        return "ItemCharacteristic{" + "bonusMap=" + bonusMap.size + ", examineMap=" + examineMap.size + '}'
    }
}
package org.redrune.game.entity.actor.player.data

import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import org.redrune.game.entity.item.ItemsContainer
import org.redrune.game.global.WorldTile
import org.redrune.game.global.map.region.RegionManager
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository
import java.io.Serializable

class PlayerInventory : Serializable {

    val items = ItemsContainer<Item>(28, false)

    @Transient
    private var player: Player? = null
    fun setPlayer(player: Player?) {
        this.player = player
    }

    fun unlockInventoryOptions() {
        player!!.packets.sendIComponentSettings(INVENTORY_INTERFACE, 0, 0, 27, 4554126)
        player!!.packets.sendIComponentSettings(INVENTORY_INTERFACE, 0, 28, 55, 2097152)
    }

    fun reset() {
        items.reset()
        init() // as all slots reseted better just send all again
    }

    fun init() {
        player!!.packets.sendItems(93, items)
    }

    fun addAll(items: ItemsContainer<Item>) {
        for (i in 0 until items.size) {
            if (items[i] != null) {
                this.items.add(items[i]!!)
            }
        }
    }

    fun refresh(items: ItemsContainer<Item>?) {
        if (items != null && player != null) {
            player!!.packets.sendItems(93, items)
        }
    }

    fun addItem(itemId: Int, amount: Int): Boolean {
        if (itemId < 0 || amount < 0 || itemId >= Misc.getItemDefinitionsSize() || !player!!.controllerManager.canAddInventoryItem(
                itemId,
                amount
            )
        ) {
            return false
        }
        val itemsBefore = items.itemsCopy
        if (!items.add(Item(itemId, amount))) {
            items.add(Item(itemId, items.freeSlots))
            player!!.packets.sendMessage("Not enough space in your inventory.")
            refreshItems(itemsBefore)
            return false
        }
        refreshItems(itemsBefore)
        return true
    }

    fun refreshItems(itemsBefore: Array<Item?>) {
        val changedSlots = IntArray(itemsBefore.size)
        var count = 0
        for (index in itemsBefore.indices) {
            if (itemsBefore[index] !== items.items[index]) {
                changedSlots[count++] = index
            }
        }
        val finalChangedSlots = IntArray(count)
        System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count)
        refresh(*finalChangedSlots)
    }

    fun refresh(vararg slots: Int) {
        player!!.packets.sendUpdateItems(93, items, *slots)
    }

    fun addItem(item: Item): Boolean {
        if (item.id < 0 || item.amount < 0 || item.id >= Misc.getItemDefinitionsSize() || !player!!.controllerManager.canAddInventoryItem(
                item.id,
                item.amount
            )
        ) {
            return false
        }
        val itemsBefore = items.itemsCopy
        if (!items.add(item)) {
            items.add(Item(item.id, items.freeSlots))
            player!!.packets.sendMessage("Not enough space in your inventory.")
            refreshItems(itemsBefore)
            return false
        }
        refreshItems(itemsBefore)
        return true
    }

    fun removeItems(vararg list: Item?): Boolean {
        if (list == null || list.size == 0) {
            return false
        }
        for (item in list) {
            if (item != null) {
                deleteItem(items.getThisItemSlot(item), item)
            }
        }
        refresh()
        return true
    }

    fun deleteItem(slot: Int, item: Item) {
        if (!player!!.controllerManager.canDeleteInventoryItem(item.id, item.amount)) {
            return
        }
        val itemsBefore = items.itemsCopy
        items.remove(slot, item)
        refreshItems(itemsBefore)
    }

    fun deleteItem(itemId: Int, amount: Int) {
        if (!player!!.controllerManager.canDeleteInventoryItem(itemId, amount)) {
            return
        }
        val itemsBefore = items.itemsCopy
        items.remove(Item(itemId, amount))
        refreshItems(itemsBefore)
    }

    fun deleteItem(item: Item) {
        if (!player!!.controllerManager.canDeleteInventoryItem(item.id, item.amount)) {
            return
        }
        val itemsBefore = items.itemsCopy
        items.remove(item)
        refreshItems(itemsBefore)
    }

    /*
	 * No refresh needed its client to who does it :p
	 */
    fun switchItem(fromSlot: Int, toSlot: Int) {
        val itemsBefore = items.itemsCopy
        val fromItem = items[fromSlot]
        val toItem = items[toSlot]
        items[fromSlot] = toItem
        items[toSlot] = fromItem
        refreshItems(itemsBefore)
    }

    fun hasFreeSlots(): Boolean {
        return items.freeSlot != -1
    }

    val freeSlots: Int
        get() = items.freeSlots

    fun getNumerOf(itemId: Int): Int {
        return items.getNumberOf(itemId)
    }

    fun getItem(slot: Int): Item? {
        return items[slot]
    }

    fun containsItems(item: Array<Item>): Boolean {
        for (i in item.indices) {
            if (!items.contains(item[i])) {
                return false
            }
        }
        return true
    }

    fun containsItems(itemIds: IntArray, ammounts: IntArray): Boolean {
        val size = if (itemIds.size > ammounts.size) ammounts.size else itemIds.size
        for (i in 0 until size) {
            if (!items.contains(Item(itemIds[i], ammounts[i]))) {
                return false
            }
        }
        return true
    }

    fun containsItem(itemId: Int, ammount: Int): Boolean {
        return items.contains(Item(itemId, ammount))
    }

    fun containsOneItem(vararg itemIds: Int): Boolean {
        for (itemId in itemIds) {
            if (items.containsOne(Item(itemId, 1))) {
                return true
            }
        }
        return false
    }

    fun numberOf(id: Int): Int {
        return items.getNumberOf(Item(id, 1))
    }

    fun sendExamine(slotId: Int) {
        if (slotId >= itemsContainerSize) {
            return
        }
        val item = items[slotId] ?: return
        player!!.packets.sendMessage(ItemCharacteristicRepository.getExamine(item.id))
    }

    val itemsContainerSize: Int
        get() = items.size

    fun addItemDrop(item: Item): Boolean {
        if (item.id < 0 || item.amount < 0 || !Misc.itemExists(item.id) || !player!!.controllerManager.canAddInventoryItem(
                item.id,
                item.amount
            )
        ) {
            return false
        }
        val itemsBefore = items.itemsCopy
        val tile: WorldTile? = player
        if (!items.add(item)) {
            if (item.definitions.isStackable) {
                RegionManager.addGroundItem(item, tile, player, true, 180, 3, 150)
            } else {
                for (i in 0 until item.amount) {
                    RegionManager.addGroundItem(Item(item.id, 1), tile, player, true, 180, 3, 150)
                }
            }
            val name = item.name
            val formattedName = name + if (item.amount > 1) if (name.endsWith("s")) "" else "s" else ""
            player!!.packets.sendMessage(item.amount.toString() + " " + formattedName + " have been dropped to your feet because your inventory was full.")
        } else {
            refreshItems(itemsBefore)
        }
        return true
    }

    companion object {
        const val INVENTORY_INTERFACE = 679
        private const val serialVersionUID = 8842800123753277093L
    }

}
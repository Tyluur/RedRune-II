package org.redrune.game.entity.actor.player.link

import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import org.redrune.game.entity.item.ItemsContainer
import org.redrune.utility.constants.ItemConstants
import org.redrune.utility.game.entity.item.GrandExchangePriceLoader
import org.redrune.utility.inject

class PriceCheckManager(private val player: Player) {

    private val priceLoader: GrandExchangePriceLoader by inject()

    private val pcInv: ItemsContainer<Item> = ItemsContainer(28, false)

    fun initPriceCheck() {
        player.interfaceManager.sendInterface(206)
        player.interfaceManager.sendInventoryInterface(207)
        sendInterItems()
        sendOptions()
        player.packets.sendGlobalConfig(728, 0)
        for (i in 0 until pcInv.size) {
            player.packets.sendGlobalConfig(700 + i, 0)
        }
        player.setCloseInterfacesEvent {
            player.inventory.items.addAll(pcInv)
            player.inventory.init()
            pcInv.clear()
        }
    }

    fun sendInterItems() {
        player.packets.sendItems(531, pcInv)
        player.packets.sendItems(93, player.inventory.items)
    }

    fun sendOptions() {
        player.packets.sendUnlockIComponentOptionSlots(206, 15, 0, 54, 0, 1, 2, 3, 4, 5, 6)
        player.packets.sendUnlockIComponentOptionSlots(207, 0, 0, 27, 0, 1, 2, 3, 4, 5)
        player.packets.sendInterSetItemsOptionsScript(
            207,
            0,
            93,
            4,
            7,
            "Add",
            "Add-5",
            "Add-10",
            "Add-All",
            "Add-X",
            "Examine"
        )
    }

    fun removeItem(clickSlotId: Int, amount: Int) {
        val slot = getSlotId(clickSlotId)
        var item = pcInv[slot] ?: return
        val itemsBefore = pcInv.itemsCopy
        val maxAmount = pcInv.getNumberOf(item)
        item = if (amount < maxAmount) {
            Item(item.id, amount)
        } else {
            Item(item.id, maxAmount)
        }
        pcInv.remove(slot, item)
        player.inventory.addItem(item)
        refreshItems(itemsBefore)
    }

    fun getSlotId(clickSlotId: Int): Int {
        return clickSlotId / 2
    }

    fun refreshItems(itemsBefore: Array<Item?>) {
        var totalPrice = 0
        val changedSlots = IntArray(itemsBefore.size)
        var count = 0
        for (index in itemsBefore.indices) {
            val item = pcInv.items[index]
            if (item != null) {
                val price = priceLoader.getPrice(item.id) ?: 1
                totalPrice += price * item.amount
            }
            if (itemsBefore[index] !== item) {
                changedSlots[count++] = index
                player.packets.sendGlobalConfig(
                    700 + index,
                    if (item == null) 0 else priceLoader.getPrice(item.id) ?: 1
                )
            }
        }
        val finalChangedSlots = IntArray(count)
        System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count)
        refresh(*finalChangedSlots)
        player.packets.sendGlobalConfig(728, totalPrice)
    }

    fun refresh(vararg slots: Int) {
        player.packets.sendUpdateItems(90, pcInv, *slots)
    }

    fun addItem(slot: Int, amount: Int) {
        var item = player.inventory.getItem(slot) ?: return
        if (!ItemConstants.isTradeable(item)) {
            player.packets.sendMessage("That item isn't tradeable.")
            return
        }
        val itemsBefore = pcInv.itemsCopy
        val maxAmount = player.inventory.items.getNumberOf(item)
        item = if (amount < maxAmount) {
            Item(item.id, amount)
        } else {
            Item(item.id, maxAmount)
        }
        pcInv.add(item)
        player.inventory.deleteItem(slot, item)
        refreshItems(itemsBefore)
    }

}
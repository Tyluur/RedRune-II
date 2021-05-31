package org.redrune.game.content.entity.actor.player.market.exchange

import org.redrune.game.entity.item.Item
import org.redrune.game.entity.item.ItemsContainer
import org.redrune.game.global.World

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
data class ExchangeOffer(

    val owner: String,

    val itemId: Int,
    var amount: Int,

    val slot: Int,
    val type: ExchangeType,
    var price: Int
) {

    /**
     * The amount of the offer that was requested initially
     */
    var amountRequested: Int = amount

    /**
     * The amount of the offer that has been processed
     */
    var amountProcessed: Int = 0

    /**
     * The amount of times this offer has been processed
     */
    var amountReceived = 0

    /**
     * If the offer has been aborted yet
     */
    var aborted: Boolean = false

    /**
     * If the offer is currently being processed
     */
    var processing = false

    /**
     * The extra cash on the offer
     */
    var surplus = 0

    /**
     * Gets the container of items to collect
     *
     * @return The items
     */
    fun getItemsToCollect(): ItemsContainer<Item> {
        val items: ItemsContainer<Item> = ItemsContainer(2, true)
        val amount = amountRequested - amountProcessed
        if (aborted) {
            when (type) {
                ExchangeType.BUY -> items.add(Item(995, price * amount))
                ExchangeType.SELL -> items.add(Item(itemId, amount))
            }
        } else {
            if (amountReceived > 0) {
                when (type) {
                    ExchangeType.BUY -> items.add(Item(itemId, amountReceived))
                    ExchangeType.SELL -> items.add(Item(995, price * amountReceived))
                }
            }
        }
        if (surplus > 0) {
            when (type) {
                ExchangeType.BUY -> items.set(1, Item(995, surplus))
                ExchangeType.SELL -> items.add(Item(995, surplus))
            }
        }
        return items
    }

    /**
     * If the offer has finished, based on whether the [.amountProcessed] is at the [.amountRequested]
     * value
     */
    fun isFinished(): Boolean {
        return amountProcessed >= amountRequested
    }

    /**
     * If this is a valid offer to be processed
     */
    fun isValid(): Boolean {
        return !isFinished() && !aborted
    }

    fun notifyUpdated() {

        val player = World.getPlayerByDisplayName(owner) ?: return
        var hasOpen = false
        if (player.interfaceManager.containsInterface(105)
            || player.interfaceManager.containsTab(105)
        ) {
            ExchangeManager.sendProgress(player)
            hasOpen = true
        }
        if (!hasOpen) {
            player.packets.sendMessage("One or more of your grand exchange offers have been updated!", true)
        }
    }

    fun getAmountPending(): Int {
        return amountRequested - amountProcessed
    }

    override fun toString(): String {
        return "Offer[owner=$owner, type=$type, slot=$slot, itemId=$itemId, amount=$amount, amountRequest=$amountRequested, amountReceived=$amountReceived, amountProcessed=$amountProcessed]"
    }

}
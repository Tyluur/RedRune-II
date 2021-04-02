package org.redrune.game.content.entity.actor.player.market.exchange

import org.redrune.game.entity.item.Item
import org.redrune.game.entity.item.ItemsContainer

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
    val price: Int
) {

    /**
     * The amount of the offer that was requested initially
     */
    val amountRequested: Int = amount

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
}
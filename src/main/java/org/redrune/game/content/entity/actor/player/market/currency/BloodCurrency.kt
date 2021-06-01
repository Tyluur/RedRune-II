package org.redrune.game.content.entity.actor.player.market.currency

import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.game.content.entity.actor.player.market.ShopCurrency
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.ItemConstants

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 6/15/2017
 */
class BloodCurrency : ShopCurrency {
    override fun name(): String {
        return "blood money"
    }

    override fun getCurrencyAmount(player: Player): Int {
        return player.inventory.items.getNumberOf(ItemConstants.COINS)
    }

    override fun reduceCurrency(player: Player, amount: Int) {
        player.inventory.deleteItem(ItemConstants.COINS, amount)
    }

    override fun getBuyPrice(itemId: Int): Int {
        when (itemId) {
            else -> {
                return ItemDefinitions.getItemDefinitions(itemId).value
            }
        }
    }

    override fun stockAmount(itemId: Int): Int {
        when (itemId) {
            15273 -> return 10
        }
        return 1
    }

    override fun itemId(): Int {
        return ItemConstants.COINS
    }
}
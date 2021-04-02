package org.redrune.engine.tick.schedule.impl

import org.redrune.engine.tick.schedule.ScheduledTask
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeManager
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeOffer
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeType
import org.redrune.game.global.World

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class ExchangeTask : ScheduledTask(6, -1) {

    override fun run() {
        val lock = Object()
        val offers = mutableListOf<ExchangeOffer>()


        World.getPlayers().filterNotNull().forEach { player ->
            for (offer in player.attributes.offers.filterNotNull()) {
                if (offer.isFinished() || offer.aborted) {
                    continue
                }
                offers.add(offer)
            }
        }

        for (offer in offers) {
            when (offer.type) {
                ExchangeType.BUY -> {
                    val autoBuy = ExchangeManager.isBuyable(offer.itemId)
                    if (autoBuy) {
                        offer.amountReceived = offer.amount
                        offer.amountProcessed = offer.amount

                        offer.notifyUpdated()
                    }
                }
                ExchangeType.SELL -> {

                }
            }
        }
    }

    companion object {

    }

}
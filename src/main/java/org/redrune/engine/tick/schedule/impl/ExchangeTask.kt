package org.redrune.engine.tick.schedule.impl

import com.github.michaelbull.logging.InlineLogger
import org.redrune.engine.tick.schedule.ScheduledTask
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeManager
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeOffer
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeType
import org.redrune.game.global.World
import kotlin.Int as Int1

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class ExchangeTask : ScheduledTask(6, -1) {

    override fun run() {
        val lock = Object()
        val offers = mutableListOf<ExchangeOffer>()


        World.getPlayers().filterNotNull().forEach { player ->
            val filteredOffers = player.attributes.offers.filterNotNull()
                .filter { offer -> !offer.isFinished() && !offer.aborted && ExchangeManager.isBuyable(offer.itemId) }

            for (offer in filteredOffers) {
                offers.add(offer)
            }
        }

        for (offer in offers) {
            when (offer.type) {
                ExchangeType.BUY -> {
                    val autoBuy = ExchangeManager.isBuyable(offer.itemId)

                    if (autoBuy) {
                        val sellOffer =
                            ExchangeOffer(offer.owner, offer.itemId, offer.amount, offer.slot, offer.type, offer.price)

                        val buyPrice: Int1 = offer.price
                        val buy: Int1 = offer.amountRequested - offer.amountReceived
                        if (offer.isFinished() || offer.aborted) {
                            continue
                        }
                        val sellPrice: Int1 = sellOffer.price
                        if (sellPrice > offer.price) {
                            continue
                        }
                        val difference = buyPrice - sellPrice
                        val sellAmount = sellOffer.amountRequested - sellOffer.amountProcessed

                        var newAmount = if (buy > sellAmount) {
                            sellAmount
                        } else {
                            buy
                        }

                        if (offer.amountReceived + newAmount > offer.amountRequested) {
                            newAmount = offer.amountRequested - offer.amountReceived
                        }

                        if (newAmount == -1) {
                            continue
                        }

                        if (difference > 0) {
                            offer.surplus = (offer.surplus + difference * newAmount)
                        }

                        offer.amountProcessed = (offer.amountProcessed + newAmount)
                        offer.amountReceived = (offer.amountReceived + newAmount)

                        offer.notifyUpdated()

                        logger.info { "Successfully automatically handled offer [offer: $offer]" }
                    }
                }
                ExchangeType.SELL -> {

                }
            }
        }
    }

    companion object {

        private val logger = InlineLogger()
    }

}
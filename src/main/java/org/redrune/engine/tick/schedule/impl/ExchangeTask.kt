package org.redrune.engine.tick.schedule.impl

import com.github.michaelbull.logging.InlineLogger
import org.redrune.engine.tick.schedule.ScheduledTask
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeManager
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeOffer
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeType
import org.redrune.game.global.World
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.stream.Collectors
import kotlin.Int as Int1

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class ExchangeTask : ScheduledTask(6, -1) {

    override fun run() {
        val lock = Object()
        val offers = mutableListOf<ExchangeOffer>()

        queuedProcess.addAll(waitingToBeAdded)
        waitingToBeAdded.clear()

        World.getPlayers().filterNotNull().forEach { player ->
            val filteredOffers = player.attributes.offers.filterNotNull()
                .filter { offer -> !offer.isFinished() && !offer.aborted }

            for (offer in filteredOffers) {
                offers.add(offer)
            }
        }

        queuedProcess.addAll(offers)

        for (offer in queuedProcess) {
            synchronized(lock) {
                when (offer.type) {
                    ExchangeType.BUY -> {
                        val autoBuy = ExchangeManager.isBuyable(offer.itemId)

                        val offers = getBarteringOffers(offer)

                        if (autoBuy) {
                            val sellOffer =
                                ExchangeOffer(
                                    offer.owner,
                                    offer.itemId,
                                    offer.amount,
                                    offer.slot,
                                    offer.type,
                                    offer.price
                                )
                            offers.add(sellOffer)
                        }
                        // the list of offer that are a barter to the current offer
                        val sortedBarters: List<ExchangeOffer> = getOffersByType(
                            offers,
                            if (offer.type == ExchangeType.BUY) ExchangeType.SELL else ExchangeType.BUY
                        )

                        for (sellOffer in sortedBarters) {
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
    }

    /**
     * Gets a list of offers which are relevant to the parameterized offer
     */
    private fun getBarteringOffers(bartered: ExchangeOffer): MutableList<ExchangeOffer> {
        val list = ArrayList(getAllOffers())
        return list.stream()
            .filter { offer: ExchangeOffer -> offer.type !== bartered.type && offer.itemId == bartered.itemId }
            .collect(Collectors.toList())
    }

    /**
     * Gets all the offers in the world
     */
    private fun getAllOffers(): List<ExchangeOffer> {
        val offers = mutableListOf<ExchangeOffer>()
        for (player in World.getPlayers().filterNotNull()) {
            for (offer in player.attributes.offers.filterNotNull()) {
                if (!offer.isValid()) {
                    continue
                }
                offers.add(offer)
            }
        }
        return offers
    }

    /**
     * Gets a list of offers by the type and sorts them according to price
     *
     * @param list
     * The list of offers
     * @param type
     * The type
     */
    private fun getOffersByType(list: List<ExchangeOffer>, type: ExchangeType): List<ExchangeOffer> {
        val offers: List<ExchangeOffer> = list.stream()
            .filter { offer: ExchangeOffer -> !offer.isFinished() && !offer.aborted && offer.type === type }
            .collect(Collectors.toList<ExchangeOffer>())
        Collections.sort(offers,
            Comparator { o1: ExchangeOffer, o2: ExchangeOffer ->
                if (type === ExchangeType.SELL) o1.price else o2.price.compareTo(if (type === ExchangeType.SELL) o2.price else o1.price)
            })
        return offers
    }

    companion object {

        private val queuedProcess = ConcurrentLinkedQueue<ExchangeOffer>()

        private val waitingToBeAdded = ConcurrentLinkedQueue<ExchangeOffer>()

        /**
         * Queues an offer to the [.waitingToBeAdded] queue
         *
         * @param offer
         * The offer to queue
         */
        fun queue(offer: ExchangeOffer?) {
            try {
                waitingToBeAdded.add(offer)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private val logger = InlineLogger()

    }

}
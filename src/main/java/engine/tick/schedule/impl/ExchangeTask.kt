package engine.tick.schedule.impl

import com.github.michaelbull.logging.InlineLogger
import engine.tick.schedule.ScheduledTask
import game.content.entity.actor.player.market.exchange.ExchangeManager
import game.content.entity.actor.player.market.exchange.ExchangeOffer
import game.content.entity.actor.player.market.exchange.ExchangeType
import game.global.World
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.function.Consumer
import java.util.stream.Collectors
import kotlin.Int as Int1

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class ExchangeTask : ScheduledTask(6, -1) {

    override fun run() {
        val lock = Object()
        val world = mutableListOf<ExchangeOffer>()

        pending.addAll(awaiting)
        awaiting.clear()

        World.getPlayers().filterNotNull().forEach { player ->
            val filteredOffers = player.attributes.offers.filterNotNull()
                .filter { offer -> !offer.isFinished() && !offer.aborted }

            for (offer in filteredOffers) {
                world.add(offer)
            }
        }

        pending.addAll(world)

        for (offer in pending) {
            synchronized(lock) {
                // the list of offer that are a barter to the current offer
                val sortedBarters = getOffersByType(
                    world,
                    if (offer.type == ExchangeType.BUY) ExchangeType.SELL else ExchangeType.BUY
                )

                when (offer.type) {
                    ExchangeType.BUY -> {
                        val autoBuy = ExchangeManager.isBuyable(offer.itemId)

                        val offers = getBarteringOffers(offer)

                        for (sellOffer in offers) {
                            val buyPrice: Int1 = offer.price
                            val buy = offer.amountRequested - offer.amountReceived

                            if (offer.isFinished() || offer.aborted) {
                                continue
                            }

                            val sellPrice = sellOffer.price
                            if (sellPrice > offer.price) {
                                continue
                            }

                            logger.info { "Successfully found sell offer [$sellOffer] for buy offer [$offer]" }

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

                            sellOffer.amountProcessed = (sellOffer.amountProcessed + newAmount)
                            sellOffer.amountReceived = (sellOffer.amountReceived + newAmount)
                            sellOffer.notifyUpdated()
                        }
                    }
                    ExchangeType.SELL -> {
                        if (sortedBarters.isNotEmpty()) {
                            sortedBarters.forEach(Consumer { offer: ExchangeOffer? -> queue(offer) })
                        }
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

        private val pending = ConcurrentLinkedQueue<ExchangeOffer>()

        private val awaiting = ConcurrentLinkedQueue<ExchangeOffer>()

        /**
         * Queues an offer to the [.waitingToBeAdded] queue
         *
         * @param offer
         * The offer to queue
         */
        fun queue(offer: ExchangeOffer?) {
            try {
                awaiting.add(offer)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private val logger = InlineLogger()

    }

}
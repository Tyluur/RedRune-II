package org.redrune.game.content.entity.actor.player.market.exchange

import com.github.michaelbull.logging.InlineLogger
import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.engine.tick.task.WorldTask
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeConfiguration.MAIN_INTERFACE
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeConfiguration.Progress.*
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import org.redrune.game.entity.item.ItemsContainer
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
object ExchangeManager {

    /**
     * The list of items that can be exchanged
     */
    private val exchangeList = mutableListOf<Int>()

    fun loadExchangeList() {
        val text = Files.readAllLines(
            File("./data/repository/item/full_exchange_list.txt").toPath(),
            Charset.defaultCharset()
        )
        for (line in text) {
            if (line.startsWith("//")) {
                continue
            }
            val split = line.split(": ".toRegex()).toTypedArray()
            exchangeList.add(split[1].toInt())
        }
        logger.info { "Registered ${exchangeList.size} items that can be bought from the grand exchange." }
    }

    /**
     * Display the main grand exchange interface with the progression of all of
     * the player's offers
     *
     * @param player
     * The player
     */
    fun open(player: Player) {
        player.stopAll()

        player.interfaceManager.closeChatBoxInterface()
        player.interfaceManager.closeInventoryInterface()

        sendMainComponentConfigs(player)

        player.packets.sendUnlockIComponentOptionSlots(MAIN_INTERFACE, 209, -1, -1, 1, 2, 3, 5, 6)
        player.packets.sendUnlockIComponentOptionSlots(MAIN_INTERFACE, 211, -1, -1, 1, 2, 3, 5, 6)

        sendProgress(player)
        player.interfaceManager.sendInterface(MAIN_INTERFACE)
        /** Closes the search bar when the interface is closed  */
        player.setCloseInterfacesEvent {
            player.temporaryAttributes.remove("exchange_offer")
            player.temporaryAttributes.remove("exchange_sell_item")
            closeSearchBar(player)
        }
    }

    /**
     * Sends the main screen configs and sets them to their default value
     *
     * @param player
     * The player to send it to
     */
    private fun sendMainComponentConfigs(player: Player) {
        player.packets.sendConfig(1112, -1)
        player.packets.sendConfig(1113, -1)
        player.packets.sendConfig(1109, -1)
        player.packets.sendConfig(1110, 0)
        player.packets.sendConfig(563, 4194304)
        player.packets.sendConfig(1112, -1)
        player.packets.sendConfig(1113, -1)
        player.packets.sendConfig(1114, 0)
        player.packets.sendConfig(1109, -1)
        player.packets.sendConfig(1110, 0)
        player.packets.sendConfig(1111, 1)
        closeSearchBar(player)
    }

    /**
     * Closes the search bar that displays the names
     *
     * @param player
     * The player to close it for
     */
    private fun closeSearchBar(player: Player) {
        player.packets.sendRunScript(571)
    }

    /**
     * Sends the progress bars information
     *
     * @param player
     * 		The player
     */
    private fun sendProgress(player: Player) {
        for (i in 0..5) {
            player.packets.sendGrandExchangeBar(i, 0, ExchangeConfiguration.Progress.RESET, 0, 0, 0);
        }

        for (offer in player.attributes.offers.filterNotNull()) {
            when (offer.type) {
                ExchangeType.BUY -> if (!offer.aborted) {
                    player.packets.sendGrandExchangeBar(
                        offer.slot,
                        offer.itemId,
                        if (offer.isFinished()) FINISHED_BUYING else BUY_PROGRESSING,
                        offer.price,
                        offer.amountProcessed,
                        offer.amountRequested
                    )
                } else {
                    player.packets.sendGrandExchangeBar(
                        offer.slot,
                        offer.itemId,
                        BUY_ABORTED,
                        offer.price,
                        offer.amountProcessed,
                        offer.amountRequested
                    )
                }
                ExchangeType.SELL -> if (!offer.aborted) {
                    player.packets.sendGrandExchangeBar(
                        offer.slot,
                        offer.itemId,
                        if (offer.isFinished()) FINISHED_SELLING else SELL_PROGRESSING,
                        offer.price,
                        offer.amountProcessed,
                        offer.amountRequested
                    )
                } else {
                    player.packets.sendGrandExchangeBar(
                        offer.slot,
                        offer.itemId,
                        SELL_ABORTED,
                        offer.price,
                        offer.amountProcessed,
                        offer.amountRequested
                    )
                }
            }
        }
    }

    /**
     * Checks if an item is buyable from the grand exchange
     */
    fun isBuyable(itemId: Int): Boolean {
        return exchangeList.contains(itemId)
    }

    /**
     * Selects them that you wish to buy
     */
    fun chooseBuyItem(player: Player, itemId: Int) {
        val definition = ItemDefinitions.getItemDefinitions(itemId) ?: return

        val examine = ItemCharacteristicRepository.getExamine(itemId)

        val description = "$examine<br><br>"

        player.packets.sendIComponentText(MAIN_INTERFACE, 143, description)

        var price = definition.value

        player.packets.sendConfig(1109, itemId)
        player.packets.sendConfig(1110, 1)
        player.packets.sendConfig(1111, price)
        player.packets.sendConfig(1114, 3)

        val amount = 1
        if (price <= 0) {
            price = 1
        }

        val offer = ExchangeOffer(
            player.username,
            itemId,
            amount,
            player.getTemporaryAttribute("exchange_slot", -1),
            ExchangeType.BUY,
            price,
        )

        player.temporaryAttributes["exchange_offer"] = offer

        player.interfaceManager.sendInterface(MAIN_INTERFACE)
    }


    /**
     * Sends the collection box to the player
     */
    fun openCollectionBox(player: Player) {
        open(player)
        for (i in 0..5) {
            sendCollectInformation(player, i)
        }
        player.interfaceManager.sendInterface(109)
        player.packets.sendUnlockIComponentOptionSlots(109, 19, 0, 2, 0, 1)
        player.packets.sendUnlockIComponentOptionSlots(109, 23, 0, 2, 0, 1)
        player.packets.sendUnlockIComponentOptionSlots(109, 27, 0, 2, 0, 1)
        player.packets.sendUnlockIComponentOptionSlots(109, 32, 0, 2, 0, 1)
        player.packets.sendUnlockIComponentOptionSlots(109, 37, 0, 2, 0, 1)
        player.packets.sendUnlockIComponentOptionSlots(109, 42, 0, 2, 0, 1)
    }

    /**
     * Sends the collection box with two slots to the player, This is based on the offer in the slot provided. The
     * offer's items to collect will display here.
     *
     * @see ExchangeOffer.getItemsToCollect
     */
    fun sendCollectInformation(player: Player, slotId: Int) {
        val offer = player.attributes.offers[slotId]

        if (offer == null) {
            val ic: ItemsContainer<Item> = ItemsContainer(2, true)
            player.packets.sendConfig(1112, slotId)
            player.packets.sendItems(523 + slotId, ic)
            return
        }

        val ic: ItemsContainer<Item> = offer.getItemsToCollect()
        player.packets.sendConfig(1113, offer.type.ordinal)
        player.packets.sendConfig(1112, slotId)
        player.packets.sendItems(523 + slotId, ic)
        player.packets.sendIComponentSettings(MAIN_INTERFACE, 206, -1, -1, 6)
        player.packets.sendIComponentSettings(MAIN_INTERFACE, 208, -1, -1, 6)

        // tasking it so it looks real - wont display right when this interface is shown anyways
        WorldTasksManager.schedule(object : WorldTask() {
            override fun run() {
                player.packets.sendIComponentText(
                    MAIN_INTERFACE,
                    143,
                    ItemCharacteristicRepository.getExamine(offer.itemId)
                )
            }
        })
    }

    private val logger = InlineLogger()

}
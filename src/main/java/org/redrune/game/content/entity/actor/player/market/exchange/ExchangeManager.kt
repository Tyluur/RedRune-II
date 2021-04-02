package org.redrune.game.content.entity.actor.player.market.exchange

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeConfiguration.MAIN_INTERFACE
import org.redrune.game.entity.actor.player.Player
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
        for (i in 0..6) {
            //     player.packets.sendGrandExchangeBar(i, 0, ExchangeConfiguration.Progress.RESET, 0, 0, 0);
        }
    }

    /**
     * Checks if an item is buyable from the grand exchange
     */
    fun isBuyable(itemId: Int): Boolean {
        return exchangeList.contains(itemId)
    }

    private val logger = InlineLogger()

}
package plugin.rsinterface

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeConfiguration.COLLECTION_INTERFACE
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeConfiguration.MAIN_INTERFACE
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeConfiguration.SELL_INTERFACE
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeType
import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class GrandExchangeInterfacePlugin : InterfacePlugin {

    override fun register() {
        registerInterfacePlugin(COLLECTION_INTERFACE, MAIN_INTERFACE, SELL_INTERFACE)
    }

    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        when (interfaceId) {
            MAIN_INTERFACE -> {
                when (componentId) {
                    31, 82, 101, 47, 63, 120 -> {
                        player.temporaryAttributes.put("exchange_slot", getSlot(componentId))
                        sendScreen(player, ExchangeType.BUY)
                    }
                }
            }
        }


        return true
    }

    /**
     * Sends the screen by the type
     *
     * @param type
     * The type of offer
     */
    fun sendScreen(player: Player, type: ExchangeType) {
        resetInterfaceConfigs(player)
        if (type === ExchangeType.SELL) {
            player.packets.sendConfig(1113, 1)
            player.interfaceManager.sendInventoryInterface(SELL_INTERFACE)
            val params = arrayOf<Any>("", "", "", "", "Offer", -1, 0, 7, 4, 93, 7012370)
            player.packets.sendRunScript(149, params)
            player.packets.sendItems(93, player.inventory.items)
            player.packets.sendHideIComponent(SELL_INTERFACE, 0, false)
            player.packets.sendIComponentSettings(SELL_INTERFACE, 18, 0, 27, 1026)
            player.packets.sendConfig(1112, (player.temporaryAttributes.get("exchange_slot") as Int?)!!)
            player.packets.sendHideIComponent(105, 196, true)
        } else {
            player.packets.sendConfig1(744, 0)
            player.packets.sendConfig(1112, (player.temporaryAttributes.get("exchange_slot") as Int?)!!)
            player.packets.sendConfig(1113, 0)
            player.packets.sendInterface(true, 752, 7, 389)
            player.packets.sendRunScript(570, "Grand Exchange Item Search")
        }
    }


    /**
     * Resets interface configurations to prepare for displaying the buy screen
     *
     * @param player
     * The player to reset it for
     */
    private fun resetInterfaceConfigs(player: Player) {
        player.packets.sendConfig2(1109, -1)
        player.packets.sendConfig2(1110, 0)
        player.packets.sendConfig2(1111, 1)
        player.packets.sendConfig2(1112, -1)
        player.packets.sendConfig2(1113, 0)
    }

    companion object {


        /**
         * Finds the slot of the button you are clicking
         *
         * @param componentId
         * The button you are clicking
         */
        private fun getSlot(componentId: Int): Int {
            return when (componentId) {
                31, 32, 19 -> 0
                47, 35, 48 -> 1
                63, 51, 64 -> 2
                82, 83, 70 -> 3
                101, 102, 89 -> 4
                120, 108, 121 -> 5
                else -> -1
            }
        }

        private val logger = InlineLogger()

    }

}

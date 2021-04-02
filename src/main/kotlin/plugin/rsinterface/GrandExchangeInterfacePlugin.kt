package plugin.rsinterface

import com.github.michaelbull.logging.InlineLogger
import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeConfiguration.COLLECTION_INTERFACE
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeConfiguration.MAIN_INTERFACE
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeConfiguration.SELL_INTERFACE
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeManager
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeManager.openCollectionBox
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeManager.sendCollectInformation
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeOffer
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeType
import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.InputEvent

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
            COLLECTION_INTERFACE -> {
                when (componentId) {

                    19 -> {
                        collectItems(
                            player,
                            0,
                            if (slotId == 0) 0 else 1,
                            if (packetId == 61) 0 else 1
                        )
                    }

                    23 -> {
                        collectItems(
                            player,
                            1,
                            if (slotId == 0) 0 else 1,
                            if (packetId == 61) 0 else 1
                        )
                    }

                    27 -> {
                        collectItems(
                            player,
                            2,
                            if (slotId == 0) 0 else 1,
                            if (packetId == 61) 0 else 1
                        )
                    }

                    32 -> {
                        collectItems(
                            player,
                            3,
                            if (slotId == 0) 0 else 1,
                            if (packetId == 61) 0 else 1
                        )
                    }

                    37 -> {
                        collectItems(
                            player,
                            4,
                            if (slotId == 0) 0 else 1,
                            if (packetId == 61) 0 else 1
                        )
                    }

                    42 -> {
                        collectItems(
                            player,
                            5,
                            if (slotId == 0) 0 else 1,
                            if (packetId == 61) 0 else 1
                        )
                    }
                }

            }
            MAIN_INTERFACE -> {
                val slot = getSlot(componentId)

                when (componentId) {
                    // buy buttons
                    31, 82, 101, 47, 63, 120 -> {
                        player.temporaryAttributes["exchange_slot"] = slot
                        sendScreen(player, ExchangeType.BUY)
                    }
                    // choose item button on buy screen
                    190 -> {
                        player.packets.sendRunScript(570, "Grand Exchange Item Search")
                    }
                    // back button
                    128 -> {
                        resetInterfaceConfigs(player);

                        player.interfaceManager.closeInventory();
                        player.interfaceManager.sendInventory();

                        val lastGameTab = player.interfaceManager.openGameTab(4); // inventory

                        player.setCloseInterfacesEvent(Runnable {
                            player.interfaceManager.sendInventory();
                            player.inventory.unlockInventoryOptions();
                            player.interfaceManager.sendEquipment();
                            player.interfaceManager.openGameTab(lastGameTab);
                        });
                        ExchangeManager.open(player);
                    }
                    // confirm button
                    186 -> {
                        val offer: ExchangeOffer =
                            (player.temporaryAttributes["exchange_offer"] ?: return true) as ExchangeOffer

                        logger.info { "Selected offer $offer" }

                        val price = (offer.amountRequested * offer.price)

                        if (price > Int.MAX_VALUE
                            || price <= 0
                            || offer.amountRequested <= 0
                            || offer.price <= 0
                        ) {
                            player.packets.sendMessage("Invalid input.")
                            return true
                        }

                        val cashAmount = price.toInt()

                        when (offer.type) {
                            ExchangeType.BUY -> {
                                if (player.takeMoney(cashAmount)) {
                                    player.attributes.offers[offer.slot] = offer
                                    ExchangeManager.open(player)
                                } else {
                                    player.packets.sendMessage(
                                        "You need to have " + Misc.format(price)
                                            .toString() + " coins to make this exchange."
                                    )
                                    return true
                                }
                                return true
                            }
                            ExchangeType.SELL -> {
                                return true
                            }
                        }
                    }

                    // goto collect buy
                    19, 35, 51, 108, 89, 70 -> {
                        player.temporaryAttributes["exchange_slot"] = slot
                        val offer = player.attributes.offers[slot] ?: return true

                        if (packetId == 61) {
                            sendCollectInformation(
                                player,
                                getSlot(componentId)
                            )
                        } else {
                            abortOffer(player, offer)
                        }
                    }

                    // collect
                    206, 208 -> {
                        val slot = player.getTemporaryAttribute("exchange_slot", 0)

                        val offer =
                            (player.attributes.offers[slot] ?: return true)

                        collectItem(player, offer, offer.itemId, packetId, componentId)
                    }
                    // abort via information screen:
                    200 -> {
                        val slot = player.getTemporaryAttribute("exchange_slot", 0)
                        val offer =
                            (player.attributes.offers[slot] ?: return true)
                        abortOffer(player, offer);
                    }

                    // +1
                    157 -> {
                        val offer: ExchangeOffer =
                            (player.temporaryAttributes["exchange_offer"] ?: return true) as ExchangeOffer
                        increaseAmount(player, offer, 1)
                    }

                    // -1
                    155 -> {

                        val offer: ExchangeOffer =
                            (player.temporaryAttributes["exchange_offer"] ?: return true) as ExchangeOffer
                        increaseAmount(player, offer, -1)
                    }

                    160 -> {
                        val offer =
                            (player.temporaryAttributes["exchange_offer"] ?: return true) as ExchangeOffer

                        if (offer.type === ExchangeType.SELL) {
                            offer.amountRequested = (1)
                            player.packets.sendConfig(1110, offer.amountRequested)
                        } else {
                            increaseAmount(player, offer, 1)
                        }
                    }

                    162 -> {
                        val offer =
                            (player.temporaryAttributes["exchange_offer"] ?: return true) as ExchangeOffer

                        if (offer.type === ExchangeType.SELL) {
                            offer.amountRequested = (10)
                            player.packets.sendConfig(1110, offer.amountRequested)
                        } else {
                            increaseAmount(player, offer, 10)
                        }

                    }

                    164 -> {
                        val offer =
                            (player.temporaryAttributes["exchange_offer"] ?: return true) as ExchangeOffer

                        if (offer.type === ExchangeType.SELL) {
                            offer.amountRequested = (100)
                            player.packets.sendConfig(1110, offer.amountRequested)
                        } else {
                            increaseAmount(player, offer, 100)
                        }

                    }

                    166 -> {
                        val offer =
                            (player.temporaryAttributes["exchange_offer"] ?: return true) as ExchangeOffer

                        if (offer.type === ExchangeType.SELL) {
                            if (player.temporaryAttributes["exchange_sell_item"] != null) {
                                val ids = player.temporaryAttributes["exchange_sell_item"] as IntArray
                                offer.amountRequested = (player.inventory.getNumerOf(ids[0]))
                            } else {
                                offer.amountRequested = (player.inventory.getNumerOf(offer.itemId))
                            }
                            player.packets.sendConfig(1110, offer.amountRequested)
                        } else {
                            increaseAmount(player, offer, 1000)
                        }

                    }

                    168 -> {
                        val offer =
                            (player.temporaryAttributes["exchange_offer"] ?: return true) as ExchangeOffer

                        player.packets.requestClientInput(object : InputEvent("Enter amount", InputEventType.INTEGER) {

                            override fun handleInput() {
                                val input: Int = this.getInput()
                                offer.amountRequested = input
                                player.packets.sendConfig(1110, offer.amountRequested)
                            }
                        })

                    }
                }
            }
        }
        return true
    }

    /**
     * Collects an item from the offer's collection exchange
     *
     * @param player
     * The player
     * @param offer
     * The offer
     * @param itemId
     * The item id
     * @param packetId
     * The packet id
     */
    private fun collectItem(player: Player, offer: ExchangeOffer, itemId: Int, packetId: Int, componentId: Int) {
        val item = offer.getItemsToCollect().lookup(itemId) ?: return
        val freeSlots = player.inventory.freeSlots

        logger.info { "Freeslots: $freeSlots" }
        if (freeSlots == 0) {
            player.packets.sendMessage("Not enough space in your inventory.")
            return
        }

        val amount = item.amount
        val slot = if (componentId == 206) 1 else 2
        val option = if (packetId == 61) 1 else 2
        var toNote = false
        if (!item.definitions.isStackable && amount > 1 && option == 1) {
            toNote = true
        }
        if (!item.definitions.isStackable && amount == 1 && option == 2) {
            toNote = true
        }

        var newId = if (toNote) ItemDefinitions.getItemDefinitions(itemId).certId else itemId
        if (newId == -1) {
            newId = itemId
        }

        val amountReq: Int = offer.amountRequested
        val received = Item(newId, amount)

        if (!player.inventory.items.hasSpaceFor(received)) {
            player.packets.sendMessage("You do not have any space in your inventory for this item.")
            return
        }

        if (slot == 1) {
            offer.amountReceived = 0
        } else {
            offer.surplus = 0
        }

        logger.info { "amountProcessed: ${offer.amountProcessed}" }
        if (offer.aborted) {
            player.attributes.offers[offer.slot] = null
            ExchangeManager.open(player)
        } else {
            if (offer.amountProcessed >= amountReq && offer.getItemsToCollect().usedSlots == 0) {
                player.attributes.offers[offer.slot] = null
                ExchangeManager.open(player)
            } else {
                sendCollectInformation(
                    player,
                    offer.slot
                )
                if (offer.getItemsToCollect().usedSlots == 0) {
                    ExchangeManager.open(player)
                }
            }
        }

        player.inventory.addItem(received)
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

    /**
     * Aborts an offer for the player
     *
     * @param player
     * The player
     * @param offer
     * The offer to abort
     */
    private fun abortOffer(player: Player, offer: ExchangeOffer) {
        if (offer.processing || offer.aborted) {
            player.packets.sendMessage("You cannot abort this offer right now...")
            return
        }
        if (offer.getItemsToCollect().usedSlots > 0) {
            player.packets.sendMessage("You need to collect your items before aborting the offer.")
            return
        }
        offer.aborted = (true)
        ExchangeManager.open(player)
        sendCollectInformation(
            player,
            offer.slot
        )
        player.packets.sendMessage("Abort request acknowledged. Please be aware that your offer may have already been completed.")
    }

    /**
     * Collects an offer from the collection box
     */
    private fun collectItems(player: Player, offerSlot: Int, itemSlot: Int, option: Int) {
        val offer: ExchangeOffer = player.attributes.offers[offerSlot]
            ?: return

        val item = offer.getItemsToCollect()[itemSlot] ?: return
        val freeSlots = player.inventory.freeSlots
        if (freeSlots == 0) {
            player.packets.sendMessage("Not enough space in your inventory.")
            return
        }
        var newId = -1
        var noted = false
        val amount = item.amount
        if (!item.definitions.isStackable && item.amount > 1 && option == 0) {
            noted = true
        }
        if (!item.definitions.isStackable && option == 1) {
            noted = true
        }
        if (noted) {
            newId = item.definitions.certId
        }
        if (newId == -1) {
            newId = item.id
        }
        val received = Item(newId, amount)
        if (!player.inventory.items.hasSpaceFor(received)) {
            player.packets.sendMessage("You don't have enough inventory space for this item.")
            return
        }
        if (itemSlot == 0) {
            offer.amountReceived = (0)
        } else {
            offer.surplus = (0)
        }
        if (offer.aborted) {
            player.attributes.offers[offer.slot] = null
            openCollectionBox(player)
        } else {
            if (offer.amountProcessed >= offer.amountRequested && offer.getItemsToCollect().usedSlots == 0) {
                player.attributes.offers[offer.slot] = null
                openCollectionBox(player)
            } else {
                openCollectionBox(player)
            }
        }
        player.inventory.addItem(received)
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

    /**
     * Increases the amount of the offer
     *
     * @param player
     * The player
     * @param offer
     * The offer
     * @param amount
     * The amount
     */
    private fun increaseAmount(player: Player, offer: ExchangeOffer?, amount: Int) {
        if (offer == null) {
            return
        }
        offer.amountRequested = (offer.amountRequested + amount)
        player.packets.sendConfig(1110, offer.amountRequested)
    }

}

package org.redrune.game.content.entity.actor.player.event.item

import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.content.entity.item.InventoryOptionsHandler
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-06
 */
class ItemInteractionEvent(
    /**
     * The item that was clicked
     */
    private val item: Item,
    /**
     * The slot of the item that was clicked
     */
    private val slotId: Int,
    /**
     * The packet that was used when clicking the item
     */
    private val packetId: Int
) : Event() {
    override fun run(player: Player) {
        val time = Misc.currentTimeMillis()
        if (player.locks.isInteractionLocked || player.emotesManager.getNextEmoteEnd() >= time) {
            return
        }
        val itemId = item.id
        when (packetId) {
            PacketConstants.ACTION_BUTTON1_PACKET -> {
                InventoryOptionsHandler.handleItemOption1(player, slotId, itemId, item)
            }
            PacketConstants.ACTION_BUTTON2_PACKET -> {
                InventoryOptionsHandler.handleItemOption2(player, slotId, itemId, item)
            }
            PacketConstants.ACTION_BUTTON3_PACKET -> {
                InventoryOptionsHandler.handleItemOption3(player, slotId, itemId, item)
            }
            PacketConstants.ACTION_BUTTON4_PACKET -> {
                InventoryOptionsHandler.handleItemOption4(player, slotId, itemId, item)
            }
            PacketConstants.ACTION_BUTTON5_PACKET -> {
                InventoryOptionsHandler.handleItemOption5(player, slotId, itemId, item)
            }
            PacketConstants.ACTION_BUTTON6_PACKET -> {
                InventoryOptionsHandler.handleItemOption6(player, slotId, itemId, item)
            }
            PacketConstants.ACTION_BUTTON7_PACKET -> {
                InventoryOptionsHandler.handleItemOption7(player, slotId, itemId, item)
            }
            PacketConstants.ACTION_BUTTON8_PACKET -> {
                InventoryOptionsHandler.handleItemOption8(player, slotId, itemId, item)
            }
        }
    }

    override fun policies(): Array<EventPolicy> {
        return arguments(EventPolicy.CLOSE_INTERFACE)
    }
}
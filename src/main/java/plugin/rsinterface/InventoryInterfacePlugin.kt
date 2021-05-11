package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import utility.constants.PacketConstants
import game.content.entity.actor.player.event.item.ItemInteractionEvent
import game.content.entity.item.InventoryOptionsHandler
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class InventoryInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        if (componentId == 0) {
            if (slotId > 27 || player.interfaceManager.containsInventoryInter()) {
                return true
            }
            val item = player.inventory.getItem(slotId)
            if (item == null || item.id != itemId) {
                return true
            }
            when (packetId) {
                PacketConstants.ACTION_BUTTON1_PACKET, PacketConstants.ACTION_BUTTON2_PACKET, PacketConstants.ACTION_BUTTON3_PACKET, PacketConstants.ACTION_BUTTON4_PACKET, PacketConstants.ACTION_BUTTON5_PACKET, PacketConstants.ACTION_BUTTON6_PACKET, PacketConstants.ACTION_BUTTON7_PACKET -> player.eventManager.start(
                    ItemInteractionEvent(item, slotId, packetId)
                )
                PacketConstants.ACTION_BUTTON8_PACKET -> InventoryOptionsHandler.handleItemOption8(
                    player,
                    slotId,
                    itemId,
                    item
                )
            }
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(679)
    }
}
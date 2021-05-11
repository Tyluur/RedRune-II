package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import utility.constants.PacketConstants
import utility.game.repository.item.ItemCharacteristicRepository
import game.content.entity.actor.player.market.Shop
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
class ShopInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        val shop = player.getTemporaryAttribute<Shop>("open_shop") ?: return true
        if (interfaceId == Shop.INTERFACE_ID) {
            if (componentId == 25) {
                when (packetId) {
                    PacketConstants.ACTION_BUTTON1_PACKET -> shop.value(player, slotId, false)
                    PacketConstants.ACTION_BUTTON2_PACKET -> shop.buy(player, slotId, 1)
                    PacketConstants.ACTION_BUTTON3_PACKET -> shop.buy(player, slotId, 5)
                    PacketConstants.ACTION_BUTTON4_PACKET -> shop.buy(player, slotId, 10)
                    PacketConstants.ACTION_BUTTON5_PACKET -> shop.buy(player, slotId, 50)
                    PacketConstants.ACTION_BUTTON9_PACKET -> shop.buy(player, slotId, 500)
                    PacketConstants.ACTION_BUTTON8_PACKET -> {
                        val item = shop.getItem(slotId / 6)
                        player.packets.sendMessage(ItemCharacteristicRepository.getExamine(item))
                    }
                }
            }
        } else if (interfaceId == Shop.INVENTORY_INTERFACE_ID) {
            if (componentId == 0) {
                when (packetId) {
                    PacketConstants.ACTION_BUTTON1_PACKET -> shop.value(player, slotId, true)
                    PacketConstants.ACTION_BUTTON2_PACKET -> shop.sell(player, slotId, 1)
                    PacketConstants.ACTION_BUTTON3_PACKET -> shop.sell(player, slotId, 5)
                    PacketConstants.ACTION_BUTTON4_PACKET -> shop.sell(player, slotId, 10)
                    PacketConstants.ACTION_BUTTON5_PACKET -> shop.sell(player, slotId, 50)
                    PacketConstants.ACTION_BUTTON9_PACKET -> player.inventory.sendExamine(slotId)
                }
            }
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(Shop.INTERFACE_ID, Shop.INVENTORY_INTERFACE_ID)
    }
}
package plugin.rsinterface

import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.game.InputEvent

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class DepositBoxInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int,
    ): Boolean {
        if (componentId == 17) {
            if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                player.bank.depositItem(slotId, 1, false)
            } else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                player.bank.depositItem(slotId, 5, false)
            } else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
                player.bank.depositItem(slotId, 10, false)
            } else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                player.bank.depositItem(slotId, Int.MAX_VALUE, false)
            } else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
                player.packets.requestClientInput(object : InputEvent("Enter Amount:", InputEventType.INTEGER) {
                    override fun handleInput() {
                        player.bank.depositItem(slotId, getInput(), false)
                    }
                })
            } else if (packetId == PacketConstants.ACTION_BUTTON9_PACKET) {
                player.inventory.sendExamine(slotId)
            }
        } else if (componentId == 18) {
            player.bank.depositAllInventory(false)
        } else if (componentId == 20) {
            player.bank.depositAllEquipment(false)
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(11)
    }
}
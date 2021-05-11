package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import utility.constants.PacketConstants
import game.entity.actor.player.Player
import utility.game.InputEvent

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/13/2017
 */
class TradeInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        if (interfaceId == 334) {
            if (componentId == 22) {
                player.closeInterfaces()
            } else if (componentId == 21) {
                player.tradeManager.accept(false)
            }
        } else if (interfaceId == 335) {
            if (componentId == 16) {
                player.tradeManager.accept(true)
            } else if (componentId == 18) {
                player.closeInterfaces()
            } else if (componentId == 31) {
                if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                    player.tradeManager.removeItem(slotId, 1)
                } else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    player.tradeManager.removeItem(slotId, 5)
                } else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
                    player.tradeManager.removeItem(slotId, 10)
                } else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                    player.tradeManager.removeItem(slotId, Int.MAX_VALUE)
                } else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
                    player.packets.requestClientInput(object : InputEvent("Enter Amount:", InputEventType.INTEGER) {
                        override fun handleInput() {
                            player.tradeManager.removeItem(slotId, getInput())
                        }
                    })
                } else if (packetId == PacketConstants.ACTION_BUTTON9_PACKET) {
                    player.tradeManager.sendValue(slotId, false)
                } else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
                    player.tradeManager.sendExamine(slotId, false)
                }
            } else if (componentId == 34) {
                if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                    player.tradeManager.sendValue(slotId, true)
                } else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
                    player.tradeManager.sendExamine(slotId, true)
                }
            }
        } else if (interfaceId == 336) {
            if (componentId == 0) {
                if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                    player.tradeManager.addItem(slotId, 1)
                } else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    player.tradeManager.addItem(slotId, 5)
                } else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
                    player.tradeManager.addItem(slotId, 10)
                } else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                    player.tradeManager.addItem(slotId, Int.MAX_VALUE)
                } else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
                    player.packets.requestClientInput(object : InputEvent("Enter Amount:", InputEventType.INTEGER) {
                        override fun handleInput() {
                            player.tradeManager.addItem(slotId, getInput())
                        }
                    })
                } else if (packetId == PacketConstants.ACTION_BUTTON9_PACKET) {
                    player.tradeManager.sendValue(slotId)
                } else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
                    player.inventory.sendExamine(slotId)
                }
            }
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(334, 335, 336)
    }
}
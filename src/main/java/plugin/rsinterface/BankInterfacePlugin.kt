package plugin.rsinterface

import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.game.InputEvent

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class BankInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int,
    ): Boolean {
        if (interfaceId == 762) {
            if (componentId == 117) {
                return true
            }
            if (componentId == 15) {
                player.bank.switchInsertItems()
            } else if (componentId == 19) {
                player.bank.switchWithdrawNotes()
            } else if (componentId == 33) {
                player.bank.depositAllInventory(true)
            } else if (componentId == 35) {
                player.bank.depositAllEquipment(true)
            } else if (componentId == 44) {
                player.closeInterfaces()
                player.interfaceManager.sendInterface(767)
                player.setCloseInterfacesEvent { player.bank.openBank() }
            } else if (componentId >= 44 && componentId <= 62) {
                val tabId = 9 - (componentId - 44) / 2
                if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                    player.bank.setCurrentTab(tabId)
                } else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    player.bank.collapse(tabId)
                }
            } else if (componentId == 93) {
                if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                    player.bank.withdrawItem(slotId, 1)
                } else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    player.bank.withdrawItem(slotId, 5)
                } else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
                    player.bank.withdrawItem(slotId, 10)
                } else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                    player.bank.withdrawLastAmount(slotId)
                } else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
                    player.packets.requestClientInput(object : InputEvent("Enter Amount:", InputEventType.INTEGER) {
                        override fun handleInput() {
                            player.bank.withdrawItem(slotId, getInput())
                        }
                    })
                    player.packets.sendRunScript(108, "Enter Amount:")
                } else if (packetId == PacketConstants.ACTION_BUTTON9_PACKET) {
                    player.bank.withdrawItem(slotId, Int.MAX_VALUE)
                } else if (packetId == PacketConstants.ACTION_BUTTON6_PACKET) {
                    player.bank.withdrawItemButOne(slotId)
                } else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
                    player.bank.sendExamine(slotId)
                }
            }
        } else if (interfaceId == 763) {
            if (componentId == 0) {
                if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                    player.bank.depositItem(slotId, 1, true)
                } else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    player.bank.depositItem(slotId, 5, true)
                } else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
                    player.bank.depositItem(slotId, 10, true)
                } else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                    player.bank.depositLastAmount(slotId)
                } else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
                    player.packets.requestClientInput(object : InputEvent("Enter Amount:", InputEventType.INTEGER) {
                        override fun handleInput() {
                            player.bank.depositItem(slotId, getInput(), true)
                        }
                    })
                } else if (packetId == PacketConstants.ACTION_BUTTON9_PACKET) {
                    player.bank.depositItem(slotId, Int.MAX_VALUE, true)
                } else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
                    player.inventory.sendExamine(slotId)
                }
            }
        } else if (interfaceId == 767) {
            if (componentId == 10) {
                player.bank.openBank()
            }
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(767, 763, 762)
    }
}
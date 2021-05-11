package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import utility.constants.PacketConstants
import game.entity.actor.npc.impl.familiar.Familiar
import game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack
import game.entity.actor.player.Player
import utility.game.InputEvent

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class FamiliarInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        if (interfaceId == 880) {
            if (componentId >= 7 && componentId <= 19) {
                Familiar.setLeftclickOption(player, (componentId - 7) / 2)
            } else if (componentId == 21) {
                Familiar.confirmLeftOption(player)
            } else if (componentId == 25) {
                Familiar.setLeftclickOption(player, 7)
            }
        } else if (interfaceId == 662) {
            if (player.familiar == null) {
                return true
            }
            if (componentId == 49) {
                player.familiar.call()
            } else if (componentId == 51) {
                player.dialogueManager.startDialogue("DismissD")
            } else if (componentId == 67) {
                player.familiar.takeBob()
            } else if (componentId == 69) {
                player.familiar.renewFamiliar()
            } else if (componentId == 74) {
                if (player.familiar.specialAttack == SpecialAttack.CLICK) {
                    player.familiar.setSpecial(true)
                }
                if (player.familiar.hasSpecialOn()) {
                    player.familiar.submitSpecial(player)
                }
            }
        } else if (interfaceId == 747) {
            if (componentId == 7) {
                Familiar.selectLeftOption(player)
            } else if (player.familiar == null) {
                return true
            }
            if (componentId == 10 || componentId == 19) {
                player.familiar.call()
            } else if (componentId == 11 || componentId == 20) {
                player.dialogueManager.startDialogue("DismissD")
            } else if (componentId == 12 || componentId == 21) {
                player.familiar.takeBob()
            } else if (componentId == 13 || componentId == 22) {
                player.familiar.renewFamiliar()
            } else if (componentId == 18) {
                player.familiar.sendFollowerDetails()
            } else if (componentId == 17) {
                if (player.familiar.specialAttack == SpecialAttack.CLICK) {
                    player.familiar.setSpecial(true)
                }
                if (player.familiar.hasSpecialOn()) {
                    player.familiar.submitSpecial(player)
                }
            }
        }
        if (interfaceId == 665) {
            if (player.familiar == null || player.familiar.bob == null) {
                return true
            }
            if (componentId == 0) {
                if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                    player.familiar.bob.addItem(slotId, 1)
                } else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    player.familiar.bob.addItem(slotId, 5)
                } else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
                    player.familiar.bob.addItem(slotId, 10)
                } else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                    player.familiar.bob.addItem(slotId, Int.MAX_VALUE)
                } else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
                    player.packets.requestClientInput(object : InputEvent("Enter Amount:", InputEventType.INTEGER) {
                        override fun handleInput() {
                            player.familiar.bob.addItem(slotId, getInput())
                        }
                    })
                } else if (packetId == PacketConstants.ACTION_BUTTON9_PACKET) {
                    player.inventory.sendExamine(slotId)
                }
            }
        } else if (interfaceId == 671) {
            if (player.familiar == null || player.familiar.bob == null) {
                return true
            }
            if (componentId == 27) {
                if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                    player.familiar.bob.removeItem(slotId, 1)
                } else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    player.familiar.bob.removeItem(slotId, 5)
                } else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
                    player.familiar.bob.removeItem(slotId, 10)
                } else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                    player.familiar.bob.removeItem(slotId, Int.MAX_VALUE)
                } else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
                    player.packets.requestClientInput(object : InputEvent("Enter Amount:", InputEventType.INTEGER) {
                        override fun handleInput() {
                            player.familiar.bob.removeItem(slotId, getInput())
                        }
                    })
                }
            } else if (componentId == 29) {
                player.familiar.takeBob()
            }
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(880, 662, 747, 665, 671)
    }
}
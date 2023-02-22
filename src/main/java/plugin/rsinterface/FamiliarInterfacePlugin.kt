package plugin.rsinterface

import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.content.entity.actor.player.design.PlayerDesign
import org.redrune.game.content.entity.actor.player.PlayerLook
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.game.InputEvent.InputEventType
import org.redrune.engine.SystemManager
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.EquipmentConstants
import org.redrune.game.entity.actor.player.data.PlayerEquipment
import plugin.rsinterface.EquipmentBonusesInterfacePlugin
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack
import org.redrune.game.content.entity.actor.player.skills.smithing.Smithing.ForgingInterface
import org.redrune.game.content.entity.actor.player.skills.SkillCapeCustomizer
import org.redrune.game.content.entity.actor.combat.function.Magic
import org.redrune.game.content.entity.actor.player.dialogue.impl.Transportation
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.utility.functions.Misc
import org.redrune.game.content.entity.actor.player.action.impl.PlayerRestAction
import plugin.rsinterface.GenieSkillSelectionInterfacePlugin
import org.redrune.game.content.entity.actor.player.event.item.ItemInteractionEvent
import org.redrune.game.content.entity.item.InventoryOptionsHandler
import org.redrune.game.content.entity.actor.player.skills.crafting.JewelrySmithing
import org.redrune.game.content.entity.actor.player.market.Shop
import org.redrune.game.content.entity.actor.player.dialogue.impl.SkillsDialogue
import org.redrune.game.content.entity.actor.player.dialogue.impl.LevelUp
import plugin.rsinterface.TeleportationInterfacePlugin.TravelLocations
import plugin.rsinterface.TeleportationInterfacePlugin
import org.redrune.utility.game.map.Coordinates
import org.redrune.game.content.entity.actor.player.controller.impl.activity.Wilderness
import org.redrune.game.content.entity.actor.player.dialogue.Dialogue
import org.redrune.utility.constants.ChatAnimations
import plugin.rsinterface.TeleportationInterfacePlugin.TransportationLocation
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.engine.tick.task.WorldTask
import org.redrune.utility.constants.MagicConstants
import org.redrune.game.entity.actor.mask.ForceTalk
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.game.InputEvent

/**
 * @author Tyluur <itstyluur@icloud.com>
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
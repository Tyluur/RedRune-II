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
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class BankInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
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
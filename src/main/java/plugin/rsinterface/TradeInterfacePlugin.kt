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
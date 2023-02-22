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
import org.redrune.game.entity.item.Item
import java.lang.StringBuilder

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/30/2017
 */
class EquipmentBonusesInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        when (interfaceId) {
            387 -> {
                if (componentId == 39) {
                    if (player.interfaceManager.containsScreenInter()) {
                        player.packets.sendMessage("Please finish what you're doing before opening your equipment bonuses.")
                        return true
                    }
                    displayEquipmentBonuses(player)
                }
                if (componentId == 42) {
                    if (player.interfaceManager.containsScreenInter()) {
                        player.packets.sendMessage("Please finish what you're doing before opening the price checker.")
                        return true
                    }
                    player.stopAll()
                    player.priceCheckManager.initPriceCheck()
                }
                if (componentId == 45) {
                    if (player.interfaceManager.containsScreenInter()) {
                        player.packets.sendMessage("Please finish what you're doing before opening your items kept on death.")
                        return true
                    }
                    player.stopAll()
                    player.interfaceManager.sendInterface(17)
                }
                // Head Gear
                if (componentId == 8 && packetId == 25) {
                    player.equipment.sendExamine(EquipmentConstants.SLOT_HAT.toInt())
                } else if (componentId == 8 && packetId == 61) {
                    EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_HAT.toInt())
                } else if (componentId == 17 && packetId == 61) {
                    EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_WEAPON.toInt())
                } else if (componentId == 17 && packetId == 25) {
                    player.equipment.sendExamine(EquipmentConstants.SLOT_WEAPON.toInt())
                } else if (componentId == 20 && packetId == 25) {
                    player.equipment.sendExamine(EquipmentConstants.SLOT_CHEST.toInt())
                } else if (componentId == 20 && packetId == 61) {
                    EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_CHEST.toInt())
                } else if (componentId == 23 && packetId == 25) {
                    player.equipment.sendExamine(EquipmentConstants.SLOT_SHIELD.toInt())
                } else if (componentId == 23 && packetId == 61) {
                    EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_SHIELD.toInt())
                } else if (componentId == 26 && packetId == 25) {
                    player.equipment.sendExamine(EquipmentConstants.SLOT_LEGS.toInt())
                } else if (componentId == 26 && packetId == 61) {
                    EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_LEGS.toInt())
                } else if (componentId == 29 && packetId == 25) {
                    player.equipment.sendExamine(EquipmentConstants.SLOT_HANDS.toInt())
                } else if (componentId == 29 && packetId == 61) {
                    EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_HANDS.toInt())
                } else if (componentId == 32 && packetId == 25) {
                    player.equipment.sendExamine(EquipmentConstants.SLOT_FEET.toInt())
                } else if (componentId == 32 && packetId == 61) {
                    EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_FEET.toInt())
                } else if (componentId == 35 && packetId == 25) {
                    player.equipment.sendExamine(EquipmentConstants.SLOT_RING.toInt())
                } else if (componentId == 35 && packetId == 61) {
                    EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_RING.toInt())
                } else if (componentId == 38 && packetId == 25) {
                    player.equipment.sendExamine(EquipmentConstants.SLOT_ARROWS.toInt())
                } else if (componentId == 38 && packetId == 61) {
                    EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_ARROWS.toInt())
                } else if (componentId == 14 && packetId == 61) {
                    EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_AMULET.toInt())
                } else if (componentId == 14 && packetId == 25) {
                    player.equipment.sendExamine(EquipmentConstants.SLOT_AMULET.toInt())
                } else if (componentId == 11 && packetId == 61) {
                    EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_CAPE.toInt())
                } else if (componentId == 11 && packetId == 25) {
                    player.equipment.sendExamine(EquipmentConstants.SLOT_CAPE.toInt())
                }
            }
            670 -> if (componentId == 0) {
                if (slotId >= player.inventory.itemsContainerSize) {
                    return true
                }
                val item = player.inventory.getItem(slotId) ?: return true
                if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                    if (EquipmentConstants.sendWear(player, slotId, item.id)) {
                        PlayerEquipment.refreshEquipBonuses(player)
                        player.packets.sendGlobalConfig(779, player.equipment.weaponRenderEmote)
                    }
                } else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
                    showStats(player, item)
                } else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                    player.inventory.sendExamine(slotId)
                }
            }
            667 -> if (componentId == 7) {
                if (slotId >= 14) {
                    return true
                }
                val item = player.equipment.getItem(slotId) ?: return true
                if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                    EquipmentConstants.sendRemove(player, slotId)
                    PlayerEquipment.refreshEquipBonuses(player)
                    player.packets.sendGlobalConfig(779, player.equipment.weaponRenderEmote)
                } else if (packetId == PacketConstants.ACTION_BUTTON10_PACKET) {
                    showStats(player, item)
                } else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
                    player.packets.sendMessage(ItemCharacteristicRepository.getExamine(item.id))
                }
            }
        }
        return true
    }

    /**
     * Displays the equipment bonuses interface
     *
     * @param player
     * The player
     */
    private fun displayEquipmentBonuses(player: Player) {
        // sent twice because of the bank glitch
        player.stopAll()
        for (i in 0..1) {
            player.packets.sendGlobalConfig(779, player.equipment.weaponRenderEmote)
            player.interfaceManager.sendInventoryInterface(670)
            player.packets.sendInterSetItemsOptionsScript(670, 0, 93, 4, 7, "Equip", "Compare", "Stats", "Examine")
            player.packets.sendUnlockIComponentOptionSlots(670, 0, 0, 27, 0, 1, 2, 3)
            player.interfaceManager.sendInterface(667)
            player.packets.sendIComponentSettings(667, 7, 0, 15, 1538)
            //			player.getPackets().sendIComponentSettings(667, 7, 0, 15, 1030);
            //			player.getPackets().sendIComponentSettings(667, 14, 0, 15, 1030);
            PlayerEquipment.refreshEquipBonuses(player)
        }
    }

    override fun register() {
        registerInterfacePlugin(387, 667, 670)
    }

    companion object {
        /**
         * Shows the stats of the item
         *
         * @param player
         * The player
         * @param item
         * The item
         */
        private fun showStats(player: Player, item: Item) {
            var bonuses = ItemCharacteristicRepository.getBonuses(item.id)
            if (bonuses == null) {
                bonuses = IntArray(18)
            }
            val titles = StringBuilder()
            val names = StringBuilder()
            val stats = StringBuilder()
            val namesArray = arrayOf(
                "Stab",
                "Slash",
                "Crush",
                "Magic",
                "Range",
                "Stab",
                "Slash",
                "Crush",
                "Magic",
                "Range",
                "Summoning",
                "Absorb Melee",
                "Absorb Magic",
                "Absorb Range",
                "Strength",
                "Ranged Str",
                "Prayer",
                "Magic Damage"
            )
            var count = 0
            var title1Done = false
            var title2Done = false
            var title3Done = false
            var namesIndentDone = false
            for (i in bonuses.indices) {
                if (bonuses[i] != 0) {
                    if (i <= 4 && !title1Done) {
                        title1Done = true
                        titles.append("Attack Bonus")
                        titles.append("                 ")
                        stats.append("<br>")
                        names.append("<br>")
                    } else if (i >= 5 && i <= 13 && !title2Done) {
                        for (j in 0..count) {
                            if (title1Done) {
                                titles.append("<br>")
                            }
                        }
                        count = 0
                        title2Done = true
                        titles.append("Defence Bonus")
                        titles.append("                 ")
                        stats.append("<br>")
                        names.append("<br>")
                    } else if (i >= 14 && !title3Done) {
                        for (j in 0..count) {
                            titles.append("<br>")
                        }
                        count = 0
                        title3Done = true
                        titles.append("Other")
                        titles.append("                 ")
                        stats.append("<br>")
                        names.append("<br>")
                    }
                    names.append(namesArray[i]).append(":")
                    if (!namesIndentDone) {
                        namesIndentDone = true
                        names.append("                       ")
                    }
                    names.append("<br>")
                    stats.append(if (bonuses[i] > 0) "+" else "").append(bonuses[i])
                    stats.append("<br>")
                    count++
                }
            }
            player.packets.sendGlobalString(321, item.name)
            player.packets.sendGlobalString(324, stats.toString())
            player.packets.sendGlobalString(323, names.toString())
            player.packets.sendGlobalString(322, titles.toString())
        }
    }
}
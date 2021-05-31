package plugin.rsinterface

import org.redrune.game.content.entity.actor.combat.function.Magic
import org.redrune.game.content.entity.actor.player.action.impl.PlayerRestAction
import org.redrune.game.content.entity.actor.player.dialogue.impl.Transportation
import org.redrune.game.content.entity.actor.player.skills.SkillCapeCustomizer
import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.EquipmentConstants
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class GameframeInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        if (interfaceId == 548 || interfaceId == 746 || interfaceId == 387) {
            if (componentId == 11) {
                if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
                    val capeId = player.equipment.capeId
                    if (capeId == 20769 || capeId == 20771) {
                        SkillCapeCustomizer.startCustomizing(player, capeId)
                    }
                } else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    val capeId = player.equipment.capeId
                    if (capeId == 20767) {
                        SkillCapeCustomizer.startCustomizing(player, capeId)
                    }
                }
            }
            if (componentId == 14) {
                val amuletId = player.equipment.amuletId
                if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
                        if (Magic.sendItemTeleportSpell(
                                player,
                                true,
                                Transportation.EMOTE,
                                Transportation.GFX,
                                4,
                                WorldTile(3087, 3496, 0)
                            )
                        ) {
                            val amulet = player.equipment.getItem(EquipmentConstants.SLOT_AMULET.toInt())
                            if (amulet != null) {
                                amulet.id = amulet.id - 2
                                player.equipment.refresh(EquipmentConstants.SLOT_AMULET.toInt())
                            }
                        }
                    } else if (amuletId == 1704 || amuletId == 10352) {
                        player.packets.sendMessage("The amulet has ran out of charges. You need to recharge it if you wish it use it once more.")
                    }
                } else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
                    if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
                        if (Magic.sendItemTeleportSpell(
                                player,
                                true,
                                Transportation.EMOTE,
                                Transportation.GFX,
                                4,
                                WorldTile(2918, 3176, 0)
                            )
                        ) {
                            val amulet = player.equipment.getItem(EquipmentConstants.SLOT_AMULET.toInt())
                            if (amulet != null) {
                                amulet.id = amulet.id - 2
                                player.equipment.refresh(EquipmentConstants.SLOT_AMULET.toInt())
                            }
                        }
                    }
                } else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                    if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
                        if (Magic.sendItemTeleportSpell(
                                player,
                                true,
                                Transportation.EMOTE,
                                Transportation.GFX,
                                4,
                                WorldTile(3105, 3251, 0)
                            )
                        ) {
                            val amulet = player.equipment.getItem(EquipmentConstants.SLOT_AMULET.toInt())
                            if (amulet != null) {
                                amulet.id = amulet.id - 2
                                player.equipment.refresh(EquipmentConstants.SLOT_AMULET.toInt())
                            }
                        }
                    }
                } else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
                    if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
                        if (Magic.sendItemTeleportSpell(
                                player,
                                true,
                                Transportation.EMOTE,
                                Transportation.GFX,
                                4,
                                WorldTile(3293, 3163, 0)
                            )
                        ) {
                            val amulet = player.equipment.getItem(EquipmentConstants.SLOT_AMULET.toInt())
                            if (amulet != null) {
                                amulet.id = amulet.id - 2
                                player.equipment.refresh(EquipmentConstants.SLOT_AMULET.toInt())
                            }
                        }
                    }
                }
            }
            if (componentId == 50) {
                if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                    EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_AURA.toInt())
                    player.auraManager.removeAura()
                } else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
                    player.equipment.sendExamine(EquipmentConstants.SLOT_AURA.toInt())
                } else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    player.auraManager.activate()
                } else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
                    player.auraManager.sendAuraRemainingTime()
                }
            }
        }
        if (interfaceId == 548 && componentId == 180 || interfaceId == 746 && componentId == 182) {
            if (player.interfaceManager.containsScreenInter() || player.interfaceManager.containsInventoryInter()) {
                player.packets.sendMessage("Please finish what you're doing before opening the world map.")
                return true
            }
            // world map open
            player.nextAnimation = Animation(840)
            player.packets.sendWindowsPane(755, 0)
            val posHash = player.x shl 14 or player.y
            player.packets.sendGlobalConfig(622, posHash) // map open
            // center
            // pos
            player.packets.sendGlobalConfig(674, posHash) // player
            // position
        } else if (interfaceId == 548 && componentId == 0 || interfaceId == 746 && componentId == 229) {
            // xp counter reset
            if (packetId == PacketConstants.ACTION_BUTTON7_PACKET) {
                player.skills.resetXpCounter()
            }
        } else if (interfaceId == 750) {
            if (componentId == 1) {
                if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                    player.attributes.toggleRun(!player.attributes.isResting)
                    if (player.attributes.isResting) {
                        player.stopAll()
                    }
                } else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    if (player.attributes.isResting) {
                        player.stopAll()
                        return true
                    }
                    val currentTime = Misc.currentTimeMillis()
                    if (player.emotesManager.getNextEmoteEnd() >= currentTime) {
                        player.packets.sendMessage("You can't rest while perfoming an emote.")
                        return true
                    }
                    if (player.locks.isLocked("emote")) {
                        player.packets.sendMessage("You can't rest while perfoming an action.")
                        return true
                    }
                    player.stopAll()
                    player.actionManager.action = PlayerRestAction()
                }
            }
        } else if (interfaceId == 751) {
            if (componentId == 25) {
                if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    player.contactManager.setPrivateStatus(0)
                } else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
                    player.contactManager.setPrivateStatus(1)
                } else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                    player.contactManager.setPrivateStatus(2)
                }
            } else if (componentId == 31) {
                if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
                    player.attributes.isFilterGame = false
                } else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                    player.attributes.isFilterGame = true
                }
            }
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(548, 746, 387, 750, 751)
    }
}
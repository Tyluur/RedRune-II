package org.redrune.game.content.entity.actor.player.event.npc

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.player.action.impl.PlayerCombatAction
import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.content.entity.actor.player.skills.fishing.Fishing.FishingSpots
import org.redrune.game.content.plugin.PluginRepository.handleItemOnNPC
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.actor.player.data.PlayerInventory
import org.redrune.game.entity.actor.player.data.RouteEvent
import org.redrune.game.global.WorldTile
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-25
 */
class NPCInterfaceInteractionEvent(
    /**
     * The npc we are casting the spell on
     */
    private val npc: NPC,
    /**
     * The details of the interface the spell is coming from
     */
    private val interfaceId: Int, private val componentId: Int, private val slot: Int
) : Event() {
    override fun run(player: Player) {
        when (interfaceId) {
            PlayerInventory.INVENTORY_INTERFACE -> {
                val item = player.inventory.getItem(slot) ?: return
                if (!player.inventory.containsItem(item.id, item.amount)) {
                    return
                }
                player.setNextFaceActor(npc)
                player.setRouteEvent(RouteEvent(npc, label@ Runnable {
                    npc.resetWalkSteps()
                    val spot = FishingSpots.forId(npc.id or 1 shl 24)
                    if (spot != null) {
                        return@Runnable
                    }
                    player.interactionManager.startInteraction(npc) // if its a spot, they dont interact with players
                    if (!player.controllerManager.processItemOnNPC(npc, item)) {
                        return@Runnable
                    }
                    if (handleItemOnNPC(player, item, npc)) {
                        return@Runnable
                    }
                    player.packets.sendMessage("Nothing interesting happens.")
                }))
            }
            662, 747 -> {
                if (player.familiar == null) {
                    return
                }
                player.resetWalkSteps()
                if (interfaceId == 747 && componentId == 14 || interfaceId == 662 && componentId == 65 || interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17 || interfaceId == 747 && componentId == 23) {
                    if (interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17) {
                        if (player.familiar.specialAttack != SpecialAttack.ENTITY) {
                            return
                        }
                    }
                    if (npc === player.familiar) {
                        player.packets.sendMessage("You can't attack your own familiar.")
                        return
                    }
                    if (!player.familiar.canAttack(npc)) {
                        player.packets.sendMessage("You can only use your familiar in a multi-zone area.")
                        return
                    } else {
                        player.familiar.setSpecial(interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17)
                        player.familiar.setTarget(npc)
                    }
                }
            }
            192, 193 -> when (componentId) {
                25, 28, 30, 32, 34, 42, 45, 49, 52, 58, 63, 70, 73, 77, 80, 84, 87, 89, 66, 67, 68, 93, 91, 99, 55, 81, 24, 20, 26, 22, 29, 33, 21, 31, 35, 27, 23, 75, 78, 82, 86, 36, 37, 38, 39 -> if (CombatAlgorithm.checkCombatSpell(
                        player,
                        componentId,
                        1,
                        false
                    )
                ) {
                    player.nextFaceWorldTile =
                        WorldTile(npc.getCoordFaceX(npc.size), npc.getCoordFaceY(npc.size), npc.plane)
                    if (!player.controllerManager.canAttack(npc)) {
                        return
                    }
                    player.setNextFaceActor(npc)
                    if (npc is Familiar) {
                        val familiar = npc
                        if (familiar === player.familiar) {
                            player.packets.sendMessage("You can't attack your own familiar.")
                            return
                        }
                        if (!familiar.canAttack(player)) {
                            player.packets.sendMessage("You can't attack this npc.")
                            return
                        }
                    } else if (!npc.isForceMultiAttacked) {
                        if (!npc.isInMultiArea || !player.isInMultiArea) {
                            if (player.attackedBy !== npc && player.attackedByDelay > Misc.currentTimeMillis()) {
                                player.packets.sendMessage("I'm already under attack.")
                                return
                            }
                            if (npc.attackedBy !== player && npc.attackedByDelay > Misc.currentTimeMillis()) {
                                player.packets.sendMessage("Someone else is already fighting that.")
                                return
                            }
                        }
                    }
                    if (!player.controllerManager.canAttack(npc)) {
                        return
                    }
                    player.actionManager.action = PlayerCombatAction(npc)
                }
            }
        }
    }

    override fun policies(): Array<EventPolicy> {
        return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK)
    }
}
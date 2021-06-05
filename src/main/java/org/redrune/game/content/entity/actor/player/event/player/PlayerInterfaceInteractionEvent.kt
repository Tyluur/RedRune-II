package org.redrune.game.content.entity.actor.player.event.player

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.player.action.impl.PlayerCombatAction
import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.content.plugin.PluginRepository.handleItemOnPlayer
import org.redrune.game.entity.actor.npc.NPC
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
class PlayerInterfaceInteractionEvent(
    /**
     * The target player we wish to cast to
     */
    private val target: Player,
    /**
     * The details of the interface we're casting from
     */
    private val interfaceId: Int,

    private val componentId: Int,

    private val slotId: Int
) : Event() {
    override fun run(player: Player) {
        when (interfaceId) {
            PlayerInventory.INVENTORY_INTERFACE -> {
                val item = player.inventory.getItem(slotId) ?: return
                if (!player.inventory.containsItem(item.id, item.amount)) {
                    return
                }
                player.setNextFaceActor(target)
                player.setRouteEvent(RouteEvent(target, Runnable {
                    if (target.interfaceManager.containsScreenInter()) {
                        player.packets.sendMessage("That player is busy at the moment.")
                        return@Runnable
                    }
                    if (!player.controllerManager.processItemOnPlayer(target, item)) {
                        return@Runnable
                    }
                    if (handleItemOnPlayer(player, item, target)) {
                        return@Runnable
                    }
                    player.packets.sendMessage("Nothing interesting happens.")
                }, true))
            }
            662, 747 -> {
                if (player.familiar == null) {
                    return
                }
                player.resetWalkSteps()
                if (interfaceId == 747 && componentId == 14 || interfaceId == 662 && componentId == 65 || interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17) {
                    if (interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17) {
                        if (player.familiar.specialAttack != SpecialAttack.ENTITY) {
                            return
                        }
                    }
                    if (!player.attributes.isCanPvp || !target.attributes.isCanPvp) {
                        player.packets.sendMessage("You can't attack players when you're not in the Wilderness..")
                        return
                    }
                    if (!player.familiar.canAttack(target)) {
                        player.packets.sendMessage("You can only use your familiar in a multi-zone area.")
                        return
                    } else {
                        player.familiar.setSpecial(interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17)
                        player.familiar.setTarget(target)
                    }
                }
            }
            193, 192 -> when (componentId) {
                25, 28, 30, 32, 34, 42, 45, 49, 52, 58, 63, 70, 73, 77, 80, 84, 87, 89, 66, 67, 68, 93, 91, 99, 55, 81, 24, 20, 26, 22, 29, 33, 21, 31, 35, 27, 23, 75, 78, 82, 86, 36, 37, 38, 39 -> if (CombatAlgorithm.checkCombatSpell(
                        player,
                        componentId,
                        1,
                        false
                    )
                ) {
                    player.nextFaceWorldTile = WorldTile(
                        target.getCoordFaceX(target.size), target.getCoordFaceY(
                            target.size
                        ), target.plane
                    )
                    if (!player.controllerManager.canAttack(target)) {
                        return
                    }
                    if (!player.attributes.isCanPvp || !target.attributes.isCanPvp) {
                        player.packets.sendMessage("You can't attack players who aren't in the Wilderness.")
                        return
                    }
                    player.setNextFaceActor(target)
                    if (!target.isInMultiArea || !player.isInMultiArea) {
                        if (player.attackedBy !== target && player.attackedByDelay > Misc.currentTimeMillis()) {
                            player.packets.sendMessage("That " + (if (player.attackedBy is Player) "player" else "npc") + " is already in combat.")
                            return
                        }
                        if (target.attackedBy != player && target.attackedByDelay > Misc.currentTimeMillis()) {
                            if (target.attackedBy is NPC) {
                                target.attackedBy = player
                            } else {
                                player.packets.sendMessage("Someone else is already fighting " + if (target.isNPC) "that." else "your opponent.")
                                return
                            }
                        }
                    }
                    if (!player.controllerManager.canAttack(target)) {
                        return
                    }
                    player.actionManager.action = PlayerCombatAction(target)
                }
            }
        }
    }

    override fun policies(): Array<EventPolicy> {
        return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK)
    }
}
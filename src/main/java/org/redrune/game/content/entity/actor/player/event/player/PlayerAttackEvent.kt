package org.redrune.game.content.entity.actor.player.event.player

import org.redrune.game.content.entity.actor.player.action.impl.PlayerCombatAction
import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-25
 */

class PlayerAttackEvent(
    /**
     * The player to attack
     */
    private val target: Player
) : Event() {
    override fun run(player: Player) {
        player.setNextFaceActor(target)
        if (!player.attributes.isCanPvp || !target.attributes.isCanPvp) {
            player.packets.sendMessage("You can't attack players when you're not in the Wilderness.")
            return
        }
        if (!target.isInMultiArea || !player.isInMultiArea) {
            if (player.attackedBy !== target && player.attackedByDelay > Misc.currentTimeMillis()) {
                player.packets.sendMessage("I'm already under attack.")
                return
            }
            if (target.attackedBy !== player && target.attackedByDelay > Misc.currentTimeMillis()) {
                if (target.attackedBy is NPC) {
                    target.attackedBy = player
                } else {
                    player.packets.sendMessage("Someone else is already fighting " + if (target.isNPC) "that." else "your opponent.")
                    return
                }
            }
        }
        player.actionManager.action = PlayerCombatAction(target)
    }

    override fun policies(): Array<EventPolicy> {
        return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK)
    }
}
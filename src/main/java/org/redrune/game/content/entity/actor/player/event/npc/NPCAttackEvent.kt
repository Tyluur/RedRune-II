package org.redrune.game.content.entity.actor.player.event.npc

import org.redrune.game.content.entity.actor.player.action.impl.PlayerCombatAction
import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-01-25
 */
class NPCAttackEvent(
    /**
     * The target we wish to attack
     */
    private val target: NPC
) : Event() {
    override fun run(player: Player) {
        player.setNextFaceActor(target)
        if (target is Familiar) {
            val familiar = target
            if (familiar === player.familiar) {
                player.packets.sendMessage("You can't attack your own familiar.")
                return
            }
            if (!familiar.canAttack(player)) {
                player.packets.sendMessage("You can't attack this target.")
                return
            }
        } else if (!target.isForceMultiAttacked) {
            if (!target.isInMultiArea || !player.isInMultiArea) {
                if (player.attackedBy !== target && player.attackedByDelay > Misc.currentTimeMillis()) {
                    player.packets.sendMessage("I'm already under attack.")
                    return
                }
                if (target.attackedBy !== player && target.attackedByDelay > Misc.currentTimeMillis()) {
                    player.packets.sendMessage("Someone else is already fighting that.")
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
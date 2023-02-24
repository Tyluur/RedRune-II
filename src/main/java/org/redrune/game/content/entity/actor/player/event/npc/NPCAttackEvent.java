package org.redrune.game.content.entity.actor.player.event.npc;

import org.redrune.game.content.entity.actor.player.action.impl.PlayerCombatAction;
import org.redrune.game.content.entity.actor.player.event.Event;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-25
 */
public class NPCAttackEvent extends Event {

    /**
     * The target we wish to attack
     */
    private final NPC target;

    public NPCAttackEvent(NPC target) {
        this.target = target;
    }

    @Override
    public void run(Player player) {
        player.setNextFaceActor(target);
        if (target instanceof Familiar) {
            Familiar familiar = (Familiar) target;
            if (familiar == player.getFamiliar()) {
                player.getPackets().sendMessage("You can't attack your own familiar.");
                return;
            }
            if (!familiar.canAttack(player)) {
                player.getPackets().sendMessage("You can't attack this target.");
                return;
            }
        } else if (!target.isForceMultiAttacked()) {
            if (!target.isInMultiArea() || !player.isInMultiArea()) {
                if (player.getAttackedBy() != target && player.getAttackedByDelay() > Misc.currentTimeMillis()) {
                    player.getPackets().sendMessage("I'm already under attack.");
                    return;
                }
                if (target.getAttackedBy() != player && target.getAttackedByDelay() > Misc.currentTimeMillis()) {
                    player.getPackets().sendMessage("Someone else is already fighting that.");
                    return;
                }
            }
        }
        player.getActionManager().setAction(new PlayerCombatAction(target));
    }

    @Override
    public EventPolicy[] policies() {
        return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK);
    }

}

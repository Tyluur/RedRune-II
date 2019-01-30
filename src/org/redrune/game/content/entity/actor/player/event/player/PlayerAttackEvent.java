package org.redrune.game.content.entity.actor.player.event.player;

import org.redrune.game.content.entity.actor.player.action.impl.PlayerCombatAction;
import org.redrune.game.content.entity.actor.player.event.Event;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-25
 */
public class PlayerAttackEvent extends Event {
	
	/**
	 * The player to attack
	 */
	private final Player target;
	
	public PlayerAttackEvent(Player target) {
		this.target = target;
	}
	
	@Override
	public void run(Player player) {
		player.setNextFaceActor(target);
		if (!player.getAttributes().isCanPvp() || !target.getAttributes().isCanPvp()) {
			player.getPackets().sendGameMessage("You can't attack players when you're not in the Wilderness.");
			return;
		}
		if (!target.isInMultiArea() || !player.isInMultiArea()) {
			if (player.getAttackedBy() != target && player.getAttackedByDelay() > Misc.currentTimeMillis()) {
				player.getPackets().sendGameMessage("I'm already under attack.");
				return;
			}
			if (target.getAttackedBy() != player && target.getAttackedByDelay() > Misc.currentTimeMillis()) {
				if (target.getAttackedBy() instanceof NPC) {
					target.setAttackedBy(player);
				} else {
					player.getPackets().sendGameMessage("Someone else is already fighting " + (target.isNPC() ? "that." : "your opponent."));
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

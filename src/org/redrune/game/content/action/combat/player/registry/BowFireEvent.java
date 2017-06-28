package org.redrune.game.content.action.combat.player.registry;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.action.combat.StaticCombatFormulae;
import org.redrune.game.content.action.combat.player.CombatTypeSwing;
import org.redrune.game.content.action.combat.player.swing.RangeCombatSwing;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public interface BowFireEvent extends CombatRegistryEvent {
	
	/**
	 * The names of the bows that can use this event
	 */
	String[] bowNames();
	
	/**
	 * Fires the bow
	 *
	 * @param attacker
	 * 		The attacker
	 * @param target
	 * 		The target
	 * @param swing
	 * 		The swing
	 * @param weaponId
	 * 		The weapon id used
	 * @param ammoId
	 * 		The id of the ammo
	 */
	void fire(Player attacker, Entity target, RangeCombatSwing swing, int weaponId, int ammoId);
	
	/**
	 * Sends the block emote 1 tick before hit appears
	 *
	 * @param target
	 * 		The target
	 * @param delay
	 * 		The delay
	 */
	// TODO: npc block emote
	default void sendBlockEmote(Entity target, int delay) {
		SystemManager.getScheduler().schedule(new ScheduledTask(delay - 1) {
			@Override
			public void run() {
				target.sendAwaitedAnimation(target.isPlayer() ? StaticCombatFormulae.getDefenceEmote(target.toPlayer()) : -1);
			}
		});
	}
	
	/**
	 * Sends the damage to the target
	 *
	 * @param attacker
	 * 		The attacker
	 * @param target
	 * 		The target
	 * @param swing
	 * 		The swing
	 * @param weaponId
	 * 		The weapon id
	 */
	default void sendDamage(Player attacker, Entity target, CombatTypeSwing swing, int weaponId) {
		final int style = attacker.getCombatDefinitions().getAttackStyle();
		final int delay = swing.getProjectileDelay(attacker, target);
		final double maxHit = swing.getMaxHit(attacker, weaponId, style, 1);
		final int damage = swing.randomizeHit(maxHit, swing.getAttackBonus(attacker, weaponId, style), swing.getDefenceBonus(target, weaponId, style));
		
		// hit, ammo, defend
		swing.applyHit(attacker, target, new Hit(attacker, damage, HitSplat.RANGE_DAMAGE).setMaxHit(maxHit), weaponId, style, delay);
		sendBlockEmote(target, delay);
	}
	
}

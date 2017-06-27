package org.redrune.game.content.action.combat.player;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.action.combat.player.registry.SpecialAttackEvent;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitAttributes;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.Misc;

/**
 * Handles the swing of the combat type.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public abstract class CombatTypeSwing {
	
	/**
	 * Handles the running of the combat type
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target we're attacking
	 * @param id
	 * 		The weapon or spell used
	 * @param combatStyle
	 * 		The combat style used
	 * @param special
	 * 		The special attack
	 */
	public abstract boolean run(Player player, org.redrune.game.node.entity.Entity target, int id, int combatStyle, SpecialAttackEvent special);
	
	/**
	 * Gets the attack bonus
	 *
	 * @param player
	 * 		The player
	 * @param weaponId
	 * 		The weapon used
	 * @param combatStyle
	 * 		The combat style used
	 */
	public abstract double getAttackBonus(Player player, int weaponId, int combatStyle);
	
	/**
	 * Gets the defence bonus
	 *
	 * @param entity
	 * 		The entity
	 * @param weaponId
	 * 		The weapon used
	 * @param combatStyle
	 * 		The combat style used
	 */
	public abstract double getDefenceBonus(org.redrune.game.node.entity.Entity entity, int weaponId, int combatStyle);
	
	/**
	 * Gets the max hit bonus
	 *
	 * @param player
	 * 		The entity
	 * @param weaponId
	 * 		The weapon used
	 * @param combatStyle
	 * 		The combat style used
	 * @param accuracyIncrease
	 * 		The accuracy increase value.
	 */
	public abstract double getMaxHit(Player player, int weaponId, int combatStyle, double accuracyIncrease);
	
	/**
	 * The combat calculator used for this swing type
	 */
	protected final CombatTypeCalculator calculator;
	
	public CombatTypeSwing(CombatTypeCalculator calculator) {
		this.calculator = calculator;
	}
	
	/**
	 * Calculates a random hit
	 *
	 * @param maxHit
	 * 		The max hit
	 * @param attackBonus
	 * 		The attack bonus
	 * @param defenceBonus
	 * 		The defence bonus
	 */
	public int randomizeHit(double maxHit, double attackBonus, double defenceBonus) {
		if (!rollHit(attackBonus, defenceBonus)) {
			return 0;
		}
		return Misc.getRandom((int) maxHit);
	}
	
	/**
	 * Calculates the two modifiers and checks if the hit should randomly miss
	 *
	 * @param attackBonus
	 * 		The attack bonus
	 * @param defenceBonus
	 * 		The defence bonus
	 */
	private boolean rollHit(double attackBonus, double defenceBonus) {
		double chance;
		if (attackBonus < defenceBonus) {
			chance = (attackBonus - 1) / (defenceBonus * 2);
		} else {
			chance = 1 - ((defenceBonus + 1) / (attackBonus * 2));
		}
		double ratio = chance * 100;
		double accuracy = Math.floor(ratio);
		double block = Math.floor(101 - ratio);
		double acc = Math.random() * accuracy;
		double def = Math.random() * block;
		return acc > def;
	}
	
	/**
	 * Applies a hit to the receiver
	 *
	 * @param attacker
	 * 		The attacking player
	 * @param receiver
	 * 		The receiver of the hit
	 * @param hit
	 * 		The hit
	 * @param itemId
	 * 		The item id used
	 * @param combatStyle
	 * 		The combat style
	 * @param delay
	 * 		The delay for the hit
	 */
	public void applyHit(Player attacker, org.redrune.game.node.entity.Entity receiver, Hit hit, int itemId, int combatStyle, int delay) {
		appendExperience(attacker, receiver, itemId, combatStyle, hit.getDamage());
		SystemManager.getScheduler().schedule(new ScheduledTask(delay, 1, false) {
			@Override
			public Runnable getTask() {
				return () -> {
					// the attribute is put when the hit actually appears
					hit.getAttributes().put(HitAttributes.WEAPON_USED, itemId);
					// and the hit is applied to the receiver
					receiver.getHitMap().applyHit(hit);
				};
			}
		});
	}
	
	/**
	 * Sends the experience task. The block emote is also sent in this block
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param params
	 * 		The parameters
	 */
	public abstract void appendExperience(Player player, org.redrune.game.node.entity.Entity target, Object... params);
	
	/**
	 * Gets the delay before a projectile can arrive at a target.
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 */
	public int getProjectileDelay(Player player, org.redrune.game.node.entity.Entity target) {
		return 1 + (int) Math.ceil(player.getLocation().getDistance(target.getLocation()) * 0.3);
	}
}

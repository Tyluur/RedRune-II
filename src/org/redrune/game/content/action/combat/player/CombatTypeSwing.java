package org.redrune.game.content.action.combat.player;

import org.redrune.game.node.entity.Entity;
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
	 * Calculates a random hit
	 *
	 * @param maxHit
	 * 		The max hit
	 * @param attackBonus
	 * 		The attack bonus
	 * @param defenceBonus
	 * 		The defence bonus
	 */
	protected int randomizeHit(double maxHit, double attackBonus, double defenceBonus) {
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
//		System.out.println((acc > def) + " attack=" + attackBonus + ", defence=" + defenceBonus + ", ratio=" + ratio + ", acc=" + acc + ", def=" + def);
		return acc > def;
	}
	
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
	 */
	public abstract void run(Player player, Entity target, int id, int combatStyle);
	
	/**
	 * The combat calculator used for this swing type
	 */
	protected final CombatTypeCalculator calculator;
	
	public CombatTypeSwing(CombatTypeCalculator calculator) {
		this.calculator = calculator;
	}
}

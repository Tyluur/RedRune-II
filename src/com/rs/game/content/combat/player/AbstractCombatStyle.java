package com.rs.game.content.combat.player;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Hit;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
public abstract class AbstractCombatStyle implements SkillConstants {
	
	/**
	 * The combat style calculator
	 */
	protected final AbstractCombatCalculator calculator;
	
	/**
	 * This method handles the swing of a combat style
	 */
	public abstract void fireSwing(Player source, Actor target);
	
	/**
	 * Handles the addition of experience
	 */
	public abstract void addExperience(Player source, Actor target, Hit hit, int attackStyle, int weaponId);
	
	/**
	 * Gets the random damage, based on the calculated maximum values
	 *
	 * @param source
	 * 		The source
	 * @param target
	 * 		The target
	 */
	public abstract int getRandomDamage(Player source, Actor target);
	
	/**
	 * Sends the hit to the target
	 */
	public abstract void sendHit(Actor source, Actor target, int maxHit, int damage);
	
	protected AbstractCombatStyle(AbstractCombatCalculator calculator) {
		this.calculator = calculator;
	}
}

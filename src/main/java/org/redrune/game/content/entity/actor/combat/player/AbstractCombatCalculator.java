package org.redrune.game.content.entity.actor.combat.player;

import org.redrune.game.entity.actor.Actor;
import org.redrune.utility.constants.BonusConstants;
import org.redrune.utility.constants.EquipmentConstants;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
public abstract class AbstractCombatCalculator implements SkillConstants, EquipmentConstants, BonusConstants {
	
	/**
	 * Gets the attack bonus of an actor
	 * @param actor
	 */
	public abstract double getAttackBonus(Actor actor);
	
	/**
	 * Gets the defence bonus of an actor
	 */
	public abstract double getDefenceBonus(Actor actor, int weaponId, int attackStyle);
	
	/**
	 * Gets the maximum hit of an
	 */
	public abstract int getMaximumHit(Actor actor, double multiplier);
}

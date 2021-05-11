package game.content.entity.actor.combat.player;

import game.entity.actor.Actor;
import utility.constants.BonusConstants;
import utility.constants.EquipmentConstants;
import utility.constants.SkillConstants;

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

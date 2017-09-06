package com.rs.game.content.combat.player;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.constants.BonusConstants;
import com.rs.utility.constants.EquipmentConstants;
import com.rs.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
public abstract class AbstractCombatCalculator implements SkillConstants, EquipmentConstants, BonusConstants {
	
	/**
	 * Gets the attack bonus of a player
	 */
	public abstract double getAttackBonus(Player player);
	
	/**
	 * Gets the defence bonus of an actor
	 */
	public abstract double getDefenceBonus(Actor actor, int weaponId, int attackStyle);
	
	/**
	 * Gets the maximum hit of a player
	 */
	public abstract int getMaximumHit(Player player, double multiplier);
}

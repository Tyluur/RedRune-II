package org.redrune.game.content.entity.actor.combat.player

import org.redrune.game.entity.actor.Actor
import org.redrune.utility.constants.BonusConstants
import org.redrune.utility.constants.EquipmentConstants
import org.redrune.utility.constants.SkillConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
abstract class AbstractCombatCalculator : SkillConstants, EquipmentConstants, BonusConstants {
    /**
     * Gets the attack bonus of an actor
     * @param actor
     */
    abstract fun getAttackBonus(actor: Actor): Double

    /**
     * Gets the defence bonus of an actor
     */
    abstract fun getDefenceBonus(actor: Actor, weaponId: Int, attackStyle: Int): Double

    /**
     * Gets the maximum hit of an
     */
    abstract fun getMaximumHit(actor: Actor, multiplier: Double): Int
}
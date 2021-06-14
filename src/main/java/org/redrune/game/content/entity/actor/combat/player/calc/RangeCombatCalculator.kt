package org.redrune.game.content.entity.actor.combat.player.calc

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatCalculator
import org.redrune.game.entity.actor.Actor
import org.redrune.utility.constants.BonusConstants.*
import org.redrune.utility.constants.EquipmentConstants.*
import org.redrune.utility.constants.SkillConstants.DEFENCE
import org.redrune.utility.constants.SkillConstants.RANGE
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/5/2017
 */
class RangeCombatCalculator : AbstractCombatCalculator() {

    override fun getAttackBonus(actor: Actor): Double {
        val weaponId = if (actor.isPlayer) actor.toPlayer().equipment.weaponId else 0
        val attackStyle =
            if (actor.isPlayer) actor.toPlayer().combatDefinitions.attackStyle else actor.toNPC().combatDefinitions.attackStyle
        val specialAttack = actor.isPlayer && actor.toPlayer().combatDefinitions.isUsingSpecialAttack
        val baseLevel =
            if (actor.isPlayer) actor.toPlayer().skills.getLevelForXp(RANGE) else actor.toNPC().combatDefinitions.rangeLevel
        val weaponRequirement = if (actor.isPlayer) actor.toPlayer().equipment.getWeaponRequirement(RANGE) else 0
        var weaponBonus = 0.0
        if (baseLevel > weaponRequirement) {
            weaponBonus = (baseLevel - weaponRequirement) * .3
        }
        val level =
            if (actor.isPlayer) actor.toPlayer().skills.getLevel(RANGE) else actor.toNPC().combatDefinitions.rangeLevel
        val prayer = if (actor.isPlayer) actor.toPlayer().prayer.rangeMultiplier else 1.0
        var additional = 1.0 // Slayer helmet/salve/...
        if (actor.isPlayer && specialAttack) {
            additional += CombatAlgorithm.getSpecialAccuracyModifier(
                if (weaponId == -1) actor.toPlayer().equipment.getIdInSlot(
                    SLOT_ARROWS.toInt()
                ) else weaponId
            )
        }
        var styleBonus = 0
        if (attackStyle == 0) {
            styleBonus = 3
        }
        val effective = floor(level * prayer * additional + styleBonus + weaponBonus)
        val bonus =
            if (actor.isPlayer) actor.toPlayer().combatDefinitions.getBonus(RANGE_ATTACK) else actor.toNPC().getBonus(
                RANGE_ATTACK
            )
        var voidAccuracy = 1.0
        if (actor.isPlayer && CombatAlgorithm.fullVoidEquipped(actor.toPlayer(), 11664, 11675)) {
            voidAccuracy = 1.1
        }
        return floor((effective + 8) * (bonus + 64) / 10).toInt() * voidAccuracy
    }

    override fun getDefenceBonus(actor: Actor, weaponId: Int, attackStyle: Int): Double {
        val styleBonus = if (attackStyle == 2) 1 else if (attackStyle == 3) 3 else 0
        val defenceLevel: Int
        val bonus: Int
        val prayer: Double
        if (actor.isPlayer) {
            val player = actor.toPlayer()
            defenceLevel = player.skills.getLevel(DEFENCE)
            prayer = player.prayer.defenceMultiplier
            bonus = player.combatDefinitions.getBonus(RANGE_DEFENCE)
        } else {
            val npc = actor.toNPC()
            defenceLevel = npc.combatLevel / 2
            prayer = 1.0
            bonus = npc.getBonus(RANGE_DEFENCE)
        }
        val effective = Math.floor(defenceLevel * prayer + styleBonus)
        return Math.floor((effective + 8) * (bonus + 64) / 10).toInt().toDouble()
    }

    override fun getMaximumHit(actor: Actor, multiplier: Double): Int {
        val attackStyle =
            if (actor.isPlayer) actor.toPlayer().combatDefinitions.attackStyle else actor.toNPC().combatDefinitions.attackStyle
        val voidEquipped = actor.isPlayer && CombatAlgorithm.fullVoidEquipped(actor.toPlayer(), 11664, 11675)
        val pernixEquipped = actor.isPlayer && CombatAlgorithm.armourSetEquipped(
            actor.toPlayer(), intArrayOf(
                SLOT_HAT.toInt(), SLOT_CHEST.toInt(), SLOT_LEGS.toInt()
            ), "pernix", "pernix", "pernix"
        )
        val level = if (actor.isPlayer) actor.toPlayer().skills.getLevel(RANGE)
            .toDouble() else actor.toNPC().combatDefinitions.rangeLevel.toDouble()
        val bonus =
            if (actor.isPlayer) actor.toPlayer().combatDefinitions.getBonus(RANGED_STRENGTH_BONUS) else actor.toNPC()
                .getBonus(
                    RANGED_STRENGTH_BONUS
                )
        val prayer = if (actor.isPlayer) actor.toPlayer().prayer.rangeMultiplier else 1.0
        var cumulativeStr = Math.floor(level * prayer)
        val styleBonus: Double = if (attackStyle == 0) 3.0 else (if (attackStyle == 1) 0 else 1.toDouble()).toDouble()
        cumulativeStr += 8 + styleBonus
        if (voidEquipped) {
            cumulativeStr *= if (actor.isPlayer && CombatAlgorithm.fullVoidEquipped(
                    actor.toPlayer(),
                    11675
                )
            ) 1.125 else 1.1
        }
        if (pernixEquipped) {
            cumulativeStr += 150.0
        }
        val effective = ((14 + cumulativeStr + bonus / 8 + cumulativeStr * bonus * 0.016865) / 10 + 1) * multiplier
        val maxHit = (effective.roundToInt() * 10).toDouble()
        return maxHit.toInt()
    }
}
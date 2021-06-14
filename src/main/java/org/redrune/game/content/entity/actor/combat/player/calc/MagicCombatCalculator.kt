package org.redrune.game.content.entity.actor.combat.player.calc

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatCalculator
import org.redrune.game.entity.actor.Actor
import org.redrune.utility.constants.BonusConstants.MAGIC_ATTACK
import org.redrune.utility.constants.BonusConstants.MAGIC_DEFENCE
import org.redrune.utility.constants.SkillConstants.DEFENCE
import org.redrune.utility.constants.SkillConstants.MAGIC
import kotlin.math.floor

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/8/2017
 */
class MagicCombatCalculator : AbstractCombatCalculator() {

    override fun getAttackBonus(actor: Actor): Double {
        // the prayer level bonus
        val level =
            if (actor.isPlayer) actor.toPlayer().skills.getLevel(MAGIC) else actor.toNPC().combatDefinitions.magicLevel
        // the prayer bonus
        val prayer = if (actor.isPlayer) actor.toPlayer().prayer.mageMultiplier else 1.0
        // the calculated boost
        val effective = Math.floor(level * prayer)
        // the bonus from your equipment
        val bonus =
            if (actor.isPlayer) actor.toPlayer().combatDefinitions.getBonus(MAGIC_ATTACK) else actor.toNPC().getBonus(
                MAGIC_ATTACK
            )
        var voidAccuracy = 1.0
        if (actor.isPlayer && CombatAlgorithm.fullVoidEquipped(actor.toPlayer(), 11663, 11674)) {
            voidAccuracy = 1.45
        }
        return floor((effective + 8) * (bonus + 64) / 10).toInt() * voidAccuracy
    }

    override fun getDefenceBonus(actor: Actor, weaponId: Int, attackStyle: Int): Double {
        // the targets defence level
        val defenceLevel: Int
        // the targets magic level
        val magicLevel: Int
        // the targets prayer boost
        val prayer: Double
        // the targets magic defence bonus
        val bonus: Double
        if (actor.isPlayer) {
            val player = actor.toPlayer()
            defenceLevel = player.skills.getLevel(DEFENCE)
            magicLevel = player.skills.getLevel(MAGIC)
            prayer = actor.toPlayer().prayer.mageMultiplier
            bonus = player.combatDefinitions.getBonus(MAGIC_DEFENCE).toDouble()
        } else {
            val npc = actor.toNPC()
            val combatLevel = npc.combatLevel
            defenceLevel = combatLevel / 2
            magicLevel = combatLevel / 2
            prayer = 1.0
            bonus = npc.getBonus(MAGIC_DEFENCE).toDouble()
        }
        // the effective calculation
        val effective = Math.floor(defenceLevel * prayer * 0.3) + magicLevel * 0.7
        // the equipment calculation [based on magic defence]
        val equipment = (bonus + 5).toInt()
        return floor((effective + 8) * (equipment + 64) / 10).toInt().toDouble()
    }

    override fun getMaximumHit(actor: Actor, multiplier: Double): Int {
        return -1
    }
}
package org.redrune.game.content.entity.actor.combat.player.calc

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatCalculator
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.data.CombatDefinitions.STAB_ATTACK
import org.redrune.game.entity.actor.data.CombatDefinitions.STRENGTH_BONUS
import org.redrune.utility.constants.EquipmentConstants.*
import org.redrune.utility.constants.SkillConstants.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
class MeleeCombatCalculator : AbstractCombatCalculator() {

    override fun getAttackBonus(actor: Actor): Double {
        val weaponId = if (actor.isPlayer) actor.toPlayer().equipment.weaponId else 0
        val attackStyle =
            if (actor.isPlayer) actor.toPlayer().combatDefinitions.attackStyle else actor.toNPC().combatDefinitions.attackStyle
        val specialAttack = actor.isPlayer && actor.toPlayer().combatDefinitions.isUsingSpecialAttack
        val style = CombatAlgorithm.getMeleeBonusStyle(weaponId, attackStyle)
        val baseLevel =
            if (actor.isPlayer) actor.toPlayer().skills.getLevelForXp(ATTACK) else actor.toNPC().combatDefinitions.attackLevel
        val weaponRequirement = if (actor.isPlayer) actor.toPlayer().equipment.getWeaponRequirement(ATTACK) else 0
        var weaponBonus = 0.0
        if (baseLevel > weaponRequirement) {
            weaponBonus = (baseLevel - weaponRequirement) * .3
        }
        val level =
            if (actor.isPlayer) actor.toPlayer().skills.getLevel(ATTACK) else actor.toNPC().combatDefinitions.attackLevel
        val prayer = if (actor.isPlayer) actor.toPlayer().prayer.attackMultiplier else 1.0
        var additional = 1.0 // Black mask/slayer helmet/salve/...
        // we add the spec modifier
        if (specialAttack) {
            additional += CombatAlgorithm.getSpecialAccuracyModifier(weaponId)
        }
        val styleBonus = if (attackStyle == 0) 3 else if (attackStyle == 2) 1 else 0
        val bonus =
            if (actor.isPlayer) actor.toPlayer().combatDefinitions.getBonus(style) else actor.toNPC().getBonus(style)
        val effective = Math.floor(level * prayer * additional + styleBonus + weaponBonus)
        var voidAccuracy = 1.0
        if (actor.isPlayer && CombatAlgorithm.fullVoidEquipped(actor.toPlayer(), 11665, 11676)) {
            voidAccuracy = 1.1
        }
        return Math.floor((effective + 8) * (bonus + 64) / 10 * voidAccuracy).toInt().toDouble()
    }

    override fun getDefenceBonus(actor: Actor, weaponId: Int, attackStyle: Int): Double {
        // find the combat style we're on for the selected type
        val meleeBonusStyle = CombatAlgorithm.getMeleeBonusStyle(weaponId, attackStyle)
        // the bonus index
        val bonusIndex = CombatAlgorithm.getMeleeDefenceBonusIndex(meleeBonusStyle)
        // the attack style of the receiver, npcs have default stab attack.
        var targetStyle = STAB_ATTACK
        // the defence level
        val defenceLevel: Int
        // the prayer boost
        val prayer: Double
        val bonus: Int
        if (actor.isPlayer) {
            val player = actor.toPlayer()
            targetStyle = player.combatDefinitions.attackStyle
            defenceLevel = player.skills.getLevel(DEFENCE)
            prayer = player.prayer.defenceMultiplier
            bonus = player.combatDefinitions.getBonus(bonusIndex)
        } else {
            val npc = actor.toNPC()
            prayer = 1.0
            defenceLevel = npc.combatLevel / 2
            bonus = npc.getBonus(bonusIndex)
        }
        // calculate the bonus of the style we're on after all the setting is done
        val styleBonus = if (targetStyle == 2) 1 else if (targetStyle == 3) 3 else 0
        val effective = Math.floor(defenceLevel * prayer + styleBonus)
        return Math.floor((effective + 8) * (bonus + 64) / 10).toInt().toDouble()
    }

    override fun getMaximumHit(actor: Actor, multiplier: Double): Int {
        val weaponId = if (actor.isPlayer) actor.toPlayer().equipment.weaponId else 0
        val attackStyle =
            if (actor.isPlayer) actor.toPlayer().combatDefinitions.attackStyle else actor.toNPC().combatDefinitions.attackStyle
        val strengthLvl = if (actor.isPlayer) actor.toPlayer().skills.getLevel(STRENGTH)
            .toDouble() else actor.toNPC().combatDefinitions.strengthLevel.toDouble()
        val xpStyle = CombatAlgorithm.getXpStyle(weaponId, attackStyle)
        val styleBonus: Double = if (xpStyle == STRENGTH) 3.0 else (if (xpStyle == -1) 1 else 0.toDouble()) as Double
        // if we use the berserk effect
        val berserk =
            actor.isPlayer && actor.toPlayer().equipment.getIdInSlot(SLOT_AMULET.toInt()) == 11128 && (weaponId == 6528 || weaponId == 6527 || weaponId == 6523 || weaponId == 6526)
        var otherBonus: Double = if (berserk) 1.20 else 1.0
        var effectiveStrength =
            8 + Math.floor(strengthLvl * if (actor.isPlayer) actor.toPlayer().prayer.strengthMultiplier else 1 + styleBonus)
        if (actor.isPlayer && CombatAlgorithm.fullVoidEquipped(actor.toPlayer(), 11665, 11676)) {
            effectiveStrength = Math.floor(effectiveStrength * 1.1)
        }
        // if we have the dharoks armour set equipped
        if (actor.isPlayer && CombatAlgorithm.armourSetEquipped(
                actor.toPlayer(),
                intArrayOf(SLOT_HAT.toInt(), SLOT_CHEST.toInt(), SLOT_LEGS.toInt(), SLOT_WEAPON.toInt()),
                "dharok",
                "dharok",
                "dharok",
                "dharok"
            )
        ) {
            val dharokMultiplier = 2 - actor.hitpoints.toDouble() / actor.maxHitpoints
                .toDouble()
            // multiplying the
            otherBonus *= dharokMultiplier
        }
        val strengthBonus = if (actor.isPlayer) actor.toPlayer().combatDefinitions.getBonus(STRENGTH_BONUS)
            .toDouble() else actor.toNPC().getBonus(STRENGTH).toDouble()
        val baseDamage = 5 + effectiveStrength * (1 + strengthBonus / 64)
        val max = Math.floor(baseDamage * multiplier * otherBonus)
        return max.toInt()
    }
}
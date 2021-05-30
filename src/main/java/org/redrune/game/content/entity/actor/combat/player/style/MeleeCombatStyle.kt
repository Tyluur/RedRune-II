package org.redrune.game.content.entity.actor.combat.player.style

import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.engine.tick.task.WorldTask
import org.redrune.engine.tick.task.WorldTasksManager.schedule
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.combat.CombatRoll
import org.redrune.game.content.entity.actor.combat.CombatSwingDetail
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.entity.actor.combat.player.calc.MeleeCombatCalculator
import org.redrune.game.content.plugin.PluginRepository.getSpecialPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.data.CombatDefinitions
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Hit
import org.redrune.game.entity.actor.mask.HitSplat
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
class MeleeCombatStyle : AbstractCombatStyle(MeleeCombatCalculator()) {
    override fun fireSwing(source: Player, target: Actor): Boolean {
        val weaponId = source.equipment.weaponId
        val combatStyle = source.combatDefinitions.attackStyle
        if (source.combatDefinitions.isUsingSpecialAttack) {
            val optional = getSpecialPlugin(weaponId)
            var energy = CombatAlgorithm.getSpecialAmount(weaponId)
            if (energy == 0 || !optional.isPresent) {
                source.packets.sendMessage("This weapon has no special attack registered; please report this on forums.")
                return false
            }
            /*       if (source.combatDefinitions.hasRingOfVigour()) {
                       energy *= TODO("Could not convert int literal '0.9' to Kotlin")
                   }*/
            source.combatDefinitions.switchUsingSpecialAttack()
            if (source.combatDefinitions.specialAttackPercentage < energy) {
                source.packets.sendMessage("You don't have enough power left.")
                return fireSwing(source, target)
            }
            val plugin = optional.get()
            plugin.fire(source, target, this)
            source.combatDefinitions.decreaseSpecialEnergy(energy)
        } else {
            val weaponName = if (weaponId == -1) "unarmed" else ItemDefinitions.getItemDefinitions(weaponId).name

            // the delay until the hitsplat appears
            val hitDelay = if (weaponId == 10887 || weaponName.toLowerCase().contains("maul") && !weaponName.startsWith(
                    "Granite"
                )
            ) 1 else 0

            // the player does the attack animation
            source.nextAnimation =
                Animation(CombatAlgorithm.getWeaponAttackEmote(weaponId, combatStyle))

            // sends the hit to the target
            sendHit(
                source,
                target,
                calculator.getMaximumHit(source, 1.0),
                getRandomDamage(source, target, 1.0),
                hitDelay
            )
        }
        return true
    }

    override fun addExperience(source: Player, target: Actor, hit: Hit, attackStyle: Int, weaponId: Int) {
        val damage = hit.damage
        val combatXp = damage / 2.5
        if (combatXp > 0) {
            source.auraManager.checkSuccefulHits(hit.damage)
            if (hit.splat == HitSplat.RANGE_DAMAGE) {
                if (attackStyle == 2) {
                    if (target.isPlayer) {
                        source.skills.addXpNoModifier(RANGE, combatXp / 2)
                        source.skills.addXpNoModifier(DEFENCE, combatXp / 2)
                    } else {
                        source.skills.addXp(RANGE, combatXp / 2)
                        source.skills.addXp(DEFENCE, combatXp / 2)
                    }
                } else {
                    if (target.isPlayer) {
                        source.skills.addXpNoModifier(RANGE, combatXp)
                    } else {
                        source.skills.addXp(RANGE, combatXp)
                    }
                }
            } else {
                val xpStyle = CombatDefinitions.getXpStyle(weaponId, attackStyle)
                if (xpStyle != CombatDefinitions.SHARED) {
                    if (target.isPlayer) {
                        source.skills.addXpNoModifier(xpStyle, combatXp)
                    } else {
                        source.skills.addXp(xpStyle, combatXp)
                    }
                } else {
                    if (target.isPlayer) {
                        source.skills.addXpNoModifier(ATTACK, combatXp / 3)
                        source.skills.addXpNoModifier(STRENGTH, combatXp / 3)
                        source.skills.addXpNoModifier(DEFENCE, combatXp / 3)
                    } else {
                        source.skills.addXp(ATTACK, combatXp / 3)
                        source.skills.addXp(STRENGTH, combatXp / 3)
                        source.skills.addXp(DEFENCE, combatXp / 3)
                    }
                }
            }
            val hpXp = damage / 7.5
            if (hpXp > 0) {
                if (target.isPlayer) {
                    source.skills.addXpNoModifier(HITPOINTS, hpXp)
                } else {
                    source.skills.addXp(HITPOINTS, hpXp)
                }
            }
        }
    }

    override fun getRandomDamage(source: Actor, target: Actor, multiplier: Double): Int {
        val weaponId = if (source.isPlayer) source.toPlayer().equipment.weaponId else 0
        val combatStyle =
            if (source.isPlayer) source.toPlayer().combatDefinitions.attackStyle else source.toNPC().combatDefinitions.attackStyle
        val maximumHit = calculator.getMaximumHit(source, multiplier) * 1.35
        val attackBonus = calculator.getAttackBonus(source) * 2.5
        val defenceBonus = calculator.getDefenceBonus(target, weaponId, combatStyle)
        return CombatRoll.randomizeHit(maximumHit, attackBonus, defenceBonus)
    }

    override fun sendHit(source: Player, target: Actor, maxHit: Int, damage: Int, delay: Int): CombatSwingDetail {
        val hit = Hit(source, damage, HitSplat.MELEE_DAMAGE).setMaxHit(maxHit)
        addExperience(source, target, hit, source.combatDefinitions.attackStyle, source.equipment.weaponId)
        target.setNextAnimationNoPriority(Animation(CombatAlgorithm.getDefenceEmote(target)))
        handleEffects(source, target, hit)
        schedule(object : WorldTask() {
            override fun run() {
                target.applyHit(hit)
            }
        }, delay)
        return CombatSwingDetail(source, target, hit)
    }
}
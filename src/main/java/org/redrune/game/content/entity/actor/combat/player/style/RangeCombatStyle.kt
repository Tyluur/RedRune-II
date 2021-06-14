package org.redrune.game.content.entity.actor.combat.player.style

import org.redrune.engine.SystemManager
import org.redrune.engine.tick.schedule.ScheduledTask
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.combat.CombatRoll
import org.redrune.game.content.entity.actor.combat.CombatSwingDetail
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.entity.actor.combat.player.calc.RangeCombatCalculator
import org.redrune.game.content.plugin.PluginRepository.getRangeWeapon
import org.redrune.game.content.plugin.PluginRepository.getSpecialPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.data.CombatDefinitions
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Hit
import org.redrune.game.entity.actor.mask.HitSplat
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.SkillConstants.*

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/5/2017
 */
class RangeCombatStyle : AbstractCombatStyle(RangeCombatCalculator()) {

    override fun fireSwing(source: Player, target: Actor): Boolean {
        val response = CombatAlgorithm.getRangeResponse(source)
        if (response == 3) {
            source.packets.sendMessage("You don't have any more ammo left to use.")
            return false
        } else if (response == 1) {
            source.packets.sendMessage("The ammo you're using is ineffective with your bow.")
            return false
        }
        val weaponId = source.equipment.weaponId
        if (source.combatDefinitions.isUsingSpecialAttack) {
            val optional = getSpecialPlugin(weaponId)
            val energy = CombatAlgorithm.getSpecialAmount(weaponId)
            if (energy == 0 || !optional.isPresent) {
                source.packets.sendMessage("This weapon has no special attack registered; please report this on forums.")
                return false
            }
            source.combatDefinitions.switchUsingSpecialAttack()
            if (source.combatDefinitions.specialAttackPercentage < energy) {
                source.packets.sendMessage("You don't have enough power left.")
                return fireSwing(source, target)
            }
            val plugin = optional.get()
            plugin.fire(source, target, this)
            source.combatDefinitions.decreaseSpecialEnergy(energy)
        } else {
            val optional = getRangeWeapon(weaponId)
            if (!optional.isPresent) {
                source.packets.sendMessage("This bow has not yet been configured, please report it on the forums.")
                return false
            }
            val event = optional.get()
            source.nextAnimation =
                Animation(CombatAlgorithm.getWeaponAttackEmote(weaponId, source.combatDefinitions.attackStyle))
            event.fire(source, target, this)
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
        return CombatRoll.randomizeHit(
            calculator.getMaximumHit(source, 1.0).toDouble(),
            calculator.getAttackBonus(source),
            calculator.getDefenceBonus(target, weaponId, combatStyle)
        )
    }

    override fun sendHit(source: Player, target: Actor, maxHit: Int, damage: Int, delay: Int): CombatSwingDetail {
        val hit = Hit(source, damage, HitSplat.RANGE_DAMAGE).setMaxHit(maxHit)
        addExperience(source, target, hit, source.combatDefinitions.attackStyle, source.equipment.weaponId)
        handleEffects(source, target, hit)
        SystemManager.SCHEDULER.schedule(object : ScheduledTask(1, delay) {
            override fun run() {
                if (ticksPassed == goalTicks - 1) {
                    target.setNextAnimationNoPriority(Animation(CombatAlgorithm.getDefenceEmote(target)))
                } else if (ticksPassed == goalTicks) {
                    target.applyHit(hit)
                    stop()
                }
            }
        })
        return CombatSwingDetail(source, target, hit)
    }
}
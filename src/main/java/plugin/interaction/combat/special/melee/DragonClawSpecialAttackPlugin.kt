package plugin.interaction.combat.special.melee

import game.content.plugin.combat.SpecialAttackPlugin
import game.entity.actor.Actor
import game.content.entity.actor.combat.player.AbstractCombatStyle
import utility.functions.Misc
import game.entity.actor.mask.*
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/5/2017
 */
class DragonClawSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(14484, 23695)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        var hits = intArrayOf(0, 1)
        var hit = style.getRandomDamage(source, target, 1.0)
        run {
            var i = 20
            while (i <= 80) {
                // all 4 d claw specs in right timing
                source.packets.sendSound(7464, i, 1)
                i += 20
            }
        }
        if (hit > 100) {
            hits = intArrayOf(hit, hit / 2, hit / 2 / 2, hit / 2 - hit / 2 / 2)
        } else {
            hit = style.getRandomDamage(source, target, 1.0)
            if (hit > 100) {
                hits = intArrayOf(0, hit, hit / 2, hit - hit / 2)
            } else {
                hit = style.getRandomDamage(source, target, 1.0)
                if (hit > 100) {
                    hits = intArrayOf(0, 0, hit / 2, hit / 2 + 10)
                } else {
                    hit = style.getRandomDamage(source, target, 1.0)
                    if (hit > 100) {
                        hits = intArrayOf(0, 0, 0, hit)
                    } else {
                        val miss = intArrayOf(Misc.random(10), Misc.random(10))
                        for (i in miss.indices) {
                            style.sendHit(source, target, style.calculator.getMaximumHit(source, 1.0), miss[i], 0)
                        }
                    }
                }
            }
        }
        for (i in hits.indices) {
            if (i > 1) {
                style.sendHit(source, target, style.calculator.getMaximumHit(source, 1.0), hits[i], 1)
            } else {
                style.sendHit(source, target, style.calculator.getMaximumHit(source, 1.0), hits[i], 0)
            }
        }
    }

    companion object {
        private val ANIMATION = Animation(10961)
        private val GRAPHICS = Graphics(1950)
    }
}
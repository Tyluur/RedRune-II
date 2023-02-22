package plugin.interaction.combat.special.melee

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/5/2017
 */
class BandosGodswordSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(11696, 23680)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        val damage = style.getRandomDamage(source, target, 1.21)
        style.sendHit(source, target, style.calculator.getMaximumHit(source, 1.21), damage, 0)
        if (target.isPlayer) {
            val targetPlayer = target.toPlayer()
            var amountLeft: Int
            if (targetPlayer.skills.drainLevel(DEFENCE, damage / 10).also { amountLeft = it } > 0) {
                if (targetPlayer.skills.drainLevel(STRENGTH, amountLeft).also { amountLeft = it } > 0) {
                    if (targetPlayer.skills.drainLevel(PRAYER, amountLeft).also { amountLeft = it } > 0) {
                        if (targetPlayer.skills.drainLevel(ATTACK, amountLeft).also { amountLeft = it } > 0) {
                            if (targetPlayer.skills.drainLevel(MAGIC, amountLeft).also { amountLeft = it } > 0) {
                                if (targetPlayer.skills.drainLevel(RANGE, amountLeft) > 0) {
                                    return
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val GRAPHICS = Graphics(2114)
        private val ANIMATION = Animation(11991)
    }
}
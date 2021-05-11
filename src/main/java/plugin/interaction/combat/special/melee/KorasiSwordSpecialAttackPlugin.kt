package plugin.interaction.combat.special.melee

import game.content.plugin.combat.SpecialAttackPlugin
import game.entity.actor.Actor
import game.content.entity.actor.combat.player.AbstractCombatStyle
import game.entity.actor.mask.*
import game.entity.actor.player.Player

/**
 * @author Tyluur
 * @since 2019-05-01
 */
class KorasiSwordSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(19784)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        if (!source.isInMultiArea) {
            var maxHit = style.calculator.getMaximumHit(source, 1.0)
            val multiplier = 0.5 + Math.random()
            maxHit *= multiplier.toInt()
            style.sendHit(source, target, maxHit, style.getRandomDamage(source, target, 1 + multiplier), 0).hit.splat =
                HitSplat.MAGIC_DAMAGE
        } else {
        }
    }

    companion object {
        private val ANIMATION = Animation(14788)
        private val GRAPHICS = Graphics(1729)
    }
}
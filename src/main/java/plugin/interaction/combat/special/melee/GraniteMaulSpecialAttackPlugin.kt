package plugin.interaction.combat.special.melee

import game.content.plugin.combat.SpecialAttackPlugin
import game.entity.actor.Actor
import game.content.entity.actor.combat.player.AbstractCombatStyle
import game.entity.actor.mask.*
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/5/2017
 */
class GraniteMaulSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(4153)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.0),
            style.getRandomDamage(source, target, 1.0),
            0
        )
    }

    override fun isInstant(): Boolean {
        return true
    }

    companion object {
        private val ANIMATION = Animation(1667)
        private val GRAPHICS = Graphics(340, 0, 96 shl 16)
    }
}
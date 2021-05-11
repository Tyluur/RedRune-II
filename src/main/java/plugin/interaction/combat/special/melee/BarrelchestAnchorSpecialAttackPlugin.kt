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
class BarrelchestAnchorSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(10887)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.1),
            style.getRandomDamage(source, target, 1.1),
            0
        )
    }

    companion object {
        private val GRAPHICS = Graphics(1027)
        private val ANIMATION = Animation(5870)
    }
}
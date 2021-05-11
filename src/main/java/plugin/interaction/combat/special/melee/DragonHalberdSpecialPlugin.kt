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
class DragonHalberdSpecialPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(3204)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = Animation(1665)
        source.setNextGraphics(GRAPHICS)
        if (target.size < 3) {
            target.setNextGraphics(GRAPHICS1)
            target.setNextGraphics(GRAPHICS2)
        }
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.1),
            style.getRandomDamage(source, target, 1.1),
            0
        )
        if (target.size > 1) {
            style.sendHit(
                source,
                target,
                style.calculator.getMaximumHit(source, 1.1),
                style.getRandomDamage(source, target, 1.1),
                1
            )
        }
    }

    companion object {
        private val GRAPHICS = Graphics(282)
        private val GRAPHICS1 = Graphics(254, 0, 100)
        private val GRAPHICS2 = Graphics(80)
    }
}
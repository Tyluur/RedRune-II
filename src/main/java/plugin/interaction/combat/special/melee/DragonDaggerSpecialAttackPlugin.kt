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
class DragonDaggerSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(1215, 5698)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.15),
            style.getRandomDamage(source, target, 1.15),
            0
        )
        if (target.isPlayer) {
            style.sendHit(
                source,
                target,
                style.calculator.getMaximumHit(source, 1.15),
                style.getRandomDamage(source, target, 1.15),
                0
            )
        } else {
            style.sendHit(
                source,
                target,
                style.calculator.getMaximumHit(source, 1.15),
                style.getRandomDamage(source, target, 1.15),
                1
            )
        }
        style.playAreaSound(source, 2537)
    }

    companion object {
        private val GRAPHICS = Graphics(252, 0, 100)
        private val ANIMATION = Animation(1062)
    }
}
package plugin.interaction.combat.special.melee

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/5/2017
 */
class ArmadylGodswordSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(11694)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.375),
            style.getRandomDamage(source, target, 1.375),
            0
        )
    }

    companion object {
        private val ANIMATION = Animation(11989)
        private val GRAPHICS = Graphics(2113)
    }
}
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
class AbyssalWhipSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(4151, 15442, 15443, 15444, 15441, 23691)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        target.setNextGraphics(GRAPHICS)
        if (target is Player) {
            val p2 = target
            p2.attributes.setRunEnergy(if (p2.attributes.runEnergy > 25) p2.attributes.runEnergy - 25 else 0)
        }
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.0),
            style.getRandomDamage(source, target, 1.0),
            0
        )
    }

    companion object {
        private val ANIMATION = Animation(11971)
        private val GRAPHICS = Graphics(2108, 0, 96)
    }
}
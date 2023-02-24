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
class ZamorakGodswordSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(11700)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        val damage = style.getRandomDamage(source, target, 1.1)
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        if (damage != 0 && target.size <= 1) {
            target.setNextGraphics(Graphics(2104))
            target.addFreezeDelay(18000, false, source) // 18seconds
        }
        style.sendHit(source, target, style.calculator.getMaximumHit(source, 1.1), damage, 0)
    }

    companion object {
        private val GRAPHICS = Graphics(1221)
        private val ANIMATION = Animation(7070)
    }
}
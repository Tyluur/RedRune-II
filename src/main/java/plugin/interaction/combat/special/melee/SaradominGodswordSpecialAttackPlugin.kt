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
class SaradominGodswordSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(11698, 23681)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        val damage = style.getRandomDamage(source, target, 1.1)
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        source.heal(damage / 2)
        source.prayer.restorePrayer(damage / 4 * 10)
        style.sendHit(source, target, style.calculator.getMaximumHit(source, 1.1), damage, 0)
    }

    companion object {
        private val ANIMATION = Animation(12019)
        private val GRAPHICS = Graphics(2109)
    }
}
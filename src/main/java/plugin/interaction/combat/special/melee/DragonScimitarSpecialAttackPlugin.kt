package plugin.interaction.combat.special.melee

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/5/2017
 */
class DragonScimitarSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(4587)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        val damage = style.getRandomDamage(source, target, 1.0)
        if (target is Player) {
            val p2 = target
            if (damage > 0) {
                p2.putTemporaryAttribute("PrayerBlocked", 5000 + Misc.currentTimeMillis())
                val prayerIds = if (p2.prayer.isAncientCurses) {
                    intArrayOf(6, 7, 8, 9, 17, 18)
                } else {
                    intArrayOf(16, 17, 18, 19)
                }
                p2.prayer.closePrayers(prayerIds)
                p2.appearance.generateAppearanceData()
                p2.prayer.recalculatePrayer()
            }
        }
        style.sendHit(source, target, style.calculator.getMaximumHit(source, 1.0), damage, 0)
        style.playAreaSound(source, 2540)
    }

    companion object {
        private val GRAPHICS = Graphics(2118)
        private val ANIMATION = Animation(12031)
    }
}
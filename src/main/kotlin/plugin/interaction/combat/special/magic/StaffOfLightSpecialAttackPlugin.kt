package plugin.interaction.combat.special.magic

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
class StaffOfLightSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(15486, 22207, 22209, 22211, 22213)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        source.setNextGraphics(GRAPHICS1)
        source.attributes.addPolDelay(60000)
    }

    override fun isInstant(): Boolean {
        return true
    }

    override fun requiresFight(): Boolean {
        return false
    }

    companion object {
        private val ANIMATION = Animation(12804)
        private val GRAPHICS = Graphics(2319)
        private val GRAPHICS1 = Graphics(2321)
    }
}
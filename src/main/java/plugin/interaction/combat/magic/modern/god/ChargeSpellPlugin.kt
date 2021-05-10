package plugin.interaction.combat.magic.modern.god

import org.redrune.engine.cycle.GameCycleWorker.Companion.ticksPassed
import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.MagicConstants.MagicBook
import org.redrune.utility.constants.key.AttributeKey

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/7/2017
 */
class ChargeSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 0
    }

    override fun animationId(): Int {
        return 0
    }

    override fun hitGfx(): Int {
        return 0
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 0
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        source.putTemporaryAttribute(AttributeKey.GOD_CHARGED, ticksPassed + 600)
    }

    override fun spellId(): Int {
        return 83
    }

    override fun exp(): Double {
        return 180.0
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }

    companion object {
        private val ANIMATION = Animation(811)
        private val GRAPHICS = Graphics(6)
    }
}
package plugin.interaction.combat.magic.modern.god

import org.redrune.engine.cycle.GameCycleWorker.Companion.ticksPassed
import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.MagicConstants.MagicBook
import org.redrune.utility.constants.key.AttributeKey

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/7/2017
 */
class ClawsOfGuthixSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 5
    }

    override fun animationId(): Int {
        return 811
    }

    override fun hitGfx(): Int {
        return 77
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return if (player.getTemporaryAttribute(
                AttributeKey.GOD_CHARGED,
                -1L
            ) >= ticksPassed
        ) {
            300
        } else {
            200
        }
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        style.sendSpell(source, target, this)
    }

    override fun spellId(): Int {
        return 67
    }

    override fun exp(): Double {
        return 34.5
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }
}
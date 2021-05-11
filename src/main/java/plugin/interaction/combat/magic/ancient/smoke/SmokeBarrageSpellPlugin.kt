package plugin.interaction.combat.magic.ancient.smoke

import game.content.entity.actor.combat.CombatSwingDetail
import game.content.entity.actor.combat.player.style.MagicCombatStyle
import game.content.plugin.combat.spell.type.CombatSpellPlugin
import game.entity.actor.Actor
import game.entity.actor.player.Player
import utility.constants.MagicConstants.MagicBook
import utility.functions.RandomFunction
import java.util.function.Consumer

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/1/2017
 */
class SmokeBarrageSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 1979
    }

    override fun hitGfx(): Int {
        return 391
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 270
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        style.sendMultiSpell(source, target, this, null, null).forEach(Consumer { spellContext: CombatSwingDetail ->
            if (spellContext.hit.damage != 0 && RandomFunction.percentageChance(10)) {
                val spellTarget = spellContext.target
                if (!spellTarget.poisonManager.isPoisoned) {
                    spellTarget.poisonManager.makePoisoned(40)
                }
            }
        })
    }

    override fun spellId(): Int {
        return 31
    }

    override fun exp(): Double {
        return 48.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }

    override fun castSoundId(): Int {
        return 183
    }

    override fun impactSoundId(): Int {
        return 185
    }
}
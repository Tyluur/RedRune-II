package plugin.interaction.combat.magic.ancient.blood

import game.content.entity.actor.combat.CombatSwingDetail
import game.content.entity.actor.combat.player.style.MagicCombatStyle
import game.content.plugin.combat.spell.type.CombatSpellPlugin
import game.entity.actor.Actor
import game.entity.actor.player.Player
import utility.constants.MagicConstants.MagicBook
import java.util.function.Consumer

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/1/2017
 */
class BloodBarrageSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 1979
    }

    override fun hitGfx(): Int {
        return 377
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 290
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        style.sendMultiSpell(source, target, this, null, null).forEach(Consumer { spellContext: CombatSwingDetail ->
            if (spellContext.hit.damage != 0) {
                source.packets.sendMessage("You drain some of your opponents' life points.")
                source.heal(spellContext.hit.damage / 4)
            }
        })
    }

    override fun spellId(): Int {
        return 27
    }

    override fun exp(): Double {
        return 51.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }
}
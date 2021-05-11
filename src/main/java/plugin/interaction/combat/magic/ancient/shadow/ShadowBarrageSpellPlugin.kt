package plugin.interaction.combat.magic.ancient.shadow

import game.content.entity.actor.combat.CombatSwingDetail
import game.content.entity.actor.combat.player.style.MagicCombatStyle
import game.content.plugin.combat.spell.type.CombatSpellPlugin
import game.entity.actor.Actor
import game.entity.actor.player.Player
import utility.constants.MagicConstants.MagicBook
import utility.constants.SkillConstants
import java.util.function.Consumer

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/1/2017
 */
class ShadowBarrageSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 1979
    }

    override fun hitGfx(): Int {
        return 383
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 280
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        style.sendMultiSpell(source, target, this, null, null).forEach(Consumer { spellDetail: CombatSwingDetail ->
            if (spellDetail.target.isPlayer) {
                spellDetail.target.toPlayer().skills.drainLevel(SkillConstants.ATTACK, 0.05, 0.15)
            }
        })
    }

    override fun spellId(): Int {
        return 35
    }

    override fun exp(): Double {
        return 49.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }

    override fun castSoundId(): Int {
        return 181
    }

    override fun impactSoundId(): Int {
        return 185
    }
}
package plugin.interaction.combat.magic.ancient.miasmic

import org.redrune.engine.SystemManager
import org.redrune.engine.tick.schedule.ScheduledTask
import org.redrune.game.content.entity.actor.combat.CombatSwingDetail
import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.MagicConstants.MagicBook
import org.redrune.utility.constants.key.AttributeKey
import java.util.function.Consumer

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/7/2017
 */
class MiasmicBarrageSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 10518
    }

    override fun hitGfx(): Int {
        return 1854
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 320
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        style.sendMultiSpell(source, target, this, null, null).forEach(Consumer { spellContext: CombatSwingDetail ->
            val spellTarget = spellContext.target
            if (!spellTarget.isPlayer || spellTarget.getTemporaryAttribute(AttributeKey.MIASMIC_IMMUNITY, false)) {
                return@Consumer
            }
            val p = spellTarget.toPlayer()
            p.packets.sendMessage("You feel slowed down.")
            spellTarget.putTemporaryAttribute(AttributeKey.MIASMIC_IMMUNITY, true)
            spellTarget.putTemporaryAttribute(AttributeKey.MIASMIC_EFFECT, true)
            SystemManager.SCHEDULER.schedule(object : ScheduledTask(1, 95) {
                override fun run() {
                    if (ticksPassed == 80) {
                        spellTarget.removeTemporaryAttribute<Any>(AttributeKey.MIASMIC_EFFECT)
                    } else if (ticksPassed == 95) {
                        spellTarget.removeTemporaryAttribute<Any>(AttributeKey.MIASMIC_IMMUNITY)
                    }
                }
            })
        })
    }

    override fun spellId(): Int {
        return 39
    }

    override fun exp(): Double {
        return 54.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }

    override fun impactSoundId(): Int {
        return 168
    }
}
package plugin.interaction.combat.magic.ancient.miasmic

import org.redrune.engine.SystemManager
import org.redrune.engine.tick.schedule.ScheduledTask
import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.utility.constants.MagicConstants.MagicBook
import org.redrune.utility.constants.key.AttributeKey

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/7/2017
 */
class MiasmicBlitzSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 10524
    }

    override fun hitGfx(): Int {
        return 1851
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 280
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                1852,
                18,
                9,
                52,
                15,
                0
            )
        )
        style.sendSpell(source, target, this, null, {
            if (!target.isPlayer || target.getTemporaryAttribute(AttributeKey.MIASMIC_IMMUNITY, false)) {
                return@sendSpell
            }
            val p = target.toPlayer()
            p.packets.sendMessage("You feel slowed down.")
            target.putTemporaryAttribute(AttributeKey.MIASMIC_IMMUNITY, true)
            target.putTemporaryAttribute(AttributeKey.MIASMIC_EFFECT, true)
            SystemManager.SCHEDULER.schedule(object : ScheduledTask(1, 75) {
                override fun run() {
                    if (ticksPassed == 60) {
                        target.removeTemporaryAttribute<Any>(AttributeKey.MIASMIC_EFFECT)
                    } else if (ticksPassed == 75) {
                        target.removeTemporaryAttribute<Any>(AttributeKey.MIASMIC_IMMUNITY)
                    }
                }
            })
        })
    }

    override fun spellId(): Int {
        return 37
    }

    override fun exp(): Double {
        return 48.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }

    override fun impactSoundId(): Int {
        return 169
    }
}
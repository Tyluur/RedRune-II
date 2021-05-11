package plugin.interaction.combat.magic.ancient.miasmic

import engine.SystemManager
import engine.tick.schedule.ScheduledTask
import game.content.entity.actor.combat.player.style.MagicCombatStyle
import game.content.plugin.combat.spell.type.CombatSpellPlugin
import game.entity.actor.Actor
import game.entity.actor.player.Player
import game.entity.projectile.ProjectileManager
import utility.constants.MagicConstants.MagicBook
import utility.constants.key.AttributeKey

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/7/2017
 */
class MiasmicRushSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 10513
    }

    override fun hitGfx(): Int {
        return 1847
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 200
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                1846,
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
            SystemManager.SCHEDULER.schedule(object : ScheduledTask(1, 35) {
                override fun run() {
                    if (ticksPassed == 20) {
                        target.removeTemporaryAttribute<Any>(AttributeKey.MIASMIC_EFFECT)
                    } else if (ticksPassed == 35) {
                        target.removeTemporaryAttribute<Any>(AttributeKey.MIASMIC_IMMUNITY)
                    }
                }
            })
        })
    }

    override fun spellId(): Int {
        return 36
    }

    override fun exp(): Double {
        return 35.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }

    override fun impactSoundId(): Int {
        return 173
    }
}
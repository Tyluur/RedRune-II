package plugin.interaction.combat.magic.ancient.smoke

import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.utility.constants.MagicConstants.MagicBook
import org.redrune.game.content.entity.actor.combat.CombatSwingDetail
import org.redrune.utility.functions.RandomFunction
import org.redrune.utility.constants.SkillConstants
import org.redrune.engine.SystemManager
import org.redrune.engine.tick.schedule.ScheduledTask
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.GameConstants
import org.redrune.utility.constants.MagicConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/1/2017
 */
class SmokeBlitzSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 1978
    }

    override fun hitGfx(): Int {
        return 387
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 230
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                386,
                18,
                9,
                52,
                15,
                0
            )
        )
        style.sendSpell(source, target, this, null, null).consume { spellContext: CombatSwingDetail ->
            if (spellContext.hit.damage != 0 && RandomFunction.percentageChance(10)) {
                val spellTarget = spellContext.target
                if (!spellTarget.poisonManager.isPoisoned) {
                    spellTarget.poisonManager.makePoisoned(40)
                }
            }
        }
    }

    override fun spellId(): Int {
        return 29
    }

    override fun exp(): Double {
        return 42.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }

    override fun castSoundId(): Int {
        return 183
    }

    override fun impactSoundId(): Int {
        return 184
    }
}
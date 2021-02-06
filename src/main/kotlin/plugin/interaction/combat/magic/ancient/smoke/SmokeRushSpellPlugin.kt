package plugin.interaction.combat.magic.ancient.smoke

import org.redrune.game.content.entity.actor.combat.CombatSwingDetail
import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.utility.constants.MagicConstants.MagicBook
import org.redrune.utility.functions.RandomFunction

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/1/2017
 */
class SmokeRushSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 1978
    }

    override fun hitGfx(): Int {
        return 385
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 150
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
                    spellTarget.poisonManager.makePoisoned(20)
                }
            }
        }
    }

    override fun spellId(): Int {
        return 28
    }

    override fun exp(): Double {
        return 30.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }

    override fun castSoundId(): Int {
        return 176
    }

    override fun impactSoundId(): Int {
        return 177
    }
}
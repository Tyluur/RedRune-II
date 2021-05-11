package plugin.interaction.combat.magic.ancient.shadow

import game.content.entity.actor.combat.CombatSwingDetail
import game.content.entity.actor.combat.player.style.MagicCombatStyle
import game.content.plugin.combat.spell.type.CombatSpellPlugin
import game.entity.actor.Actor
import game.entity.actor.player.Player
import game.entity.projectile.ProjectileManager
import utility.constants.MagicConstants.MagicBook
import utility.constants.SkillConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/1/2017
 */
class ShadowRushSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 1978
    }

    override fun hitGfx(): Int {
        return 379
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 160
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                380,
                18,
                9,
                52,
                15,
                0
            )
        )
        style.sendSpell(source, target, this, null, null).consume { spellDetail: CombatSwingDetail ->
            if (spellDetail.hit.damage != 0 && spellDetail.target.isPlayer) {
                spellDetail.target.toPlayer().skills.drainLevel(SkillConstants.ATTACK, 0.05, 0.10)
            }
        }
    }

    override fun spellId(): Int {
        return 32
    }

    override fun exp(): Double {
        return 31.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }

    override fun castSoundId(): Int {
        return 175
    }

    override fun impactSoundId(): Int {
        return 176
    }
}
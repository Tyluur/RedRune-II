package plugin.interaction.combat.magic.modern.curse

import game.content.entity.actor.combat.player.style.MagicCombatStyle
import game.content.plugin.combat.spell.type.CombatSpellPlugin
import game.entity.actor.Actor
import game.entity.actor.mask.Graphics
import game.entity.actor.player.Player
import game.entity.projectile.ProjectileManager
import utility.constants.MagicConstants.MagicBook
import utility.constants.SkillConstants

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/7/2017
 */
class VulernabilitySpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 5
    }

    override fun animationId(): Int {
        return 729
    }

    override fun hitGfx(): Int {
        return 169
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 10
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        source.setNextGraphics(GRAPHICS)
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                168,
                30,
                26,
                52,
                0,
                0
            )
        )
        style.sendSpell(source, target, this, {
            if (target.isPlayer) {
                target.toPlayer().skills.drainLevel(SkillConstants.DEFENCE, 0.10, 0.10)
            }
        }, null)
    }

    override fun spellId(): Int {
        return 75
    }

    override fun exp(): Double {
        return 76.0
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }

    override fun castSoundId(): Int {
        return 119
    }

    override fun impactSoundId(): Int {
        return castSoundId() - 1
    }

    companion object {
        private val GRAPHICS = Graphics(169)
    }
}
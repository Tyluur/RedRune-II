package plugin.interaction.combat.magic.modern.god

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
class StormOfArmadylSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return if (player.equipment.weaponId == 21777) 4 else 5
    }

    override fun animationId(): Int {
        return 10546
    }

    override fun hitGfx(): Int {
        return 1019
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 160 + minimumHit(player)
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        source.setNextGraphics(GRAPHICS)
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                1019,
                30,
                26,
                52,
                0,
                0
            )
        )
        if (target.isPlayer) {
            target.toPlayer().skills[SkillConstants.DEFENCE] =
                target.toPlayer().skills.getLevel(SkillConstants.DEFENCE) - 1
        }
        style.sendSpell(source, target, this)
    }

    override fun minimumHit(player: Player): Int {
        val start = 77
        val level = player.skills.getLevelForXp(SkillConstants.MAGIC)
        val difference = level - start
        return difference / 2 * 10
    }

    override fun spellId(): Int {
        return 99
    }

    override fun exp(): Double {
        return 70.0
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }

    companion object {
        private val GRAPHICS = Graphics(457)
    }
}
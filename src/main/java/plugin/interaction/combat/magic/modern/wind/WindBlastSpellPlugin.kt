package plugin.interaction.combat.magic.modern.wind

import game.content.entity.actor.combat.player.style.MagicCombatStyle
import game.content.plugin.combat.spell.type.CombatSpellPlugin
import game.entity.actor.Actor
import game.entity.actor.player.Player
import game.entity.projectile.ProjectileManager
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/7/2017
 */
class WindBlastSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 5
    }

    override fun animationId(): Int {
        return 14221
    }

    override fun hitGfx(): Int {
        return 2700
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 130
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                2699,
                30,
                26,
                52,
                0,
                0
            )
        )
        style.sendSpell(source, target, this)
    }

    override fun spellId(): Int {
        return 49
    }

    override fun exp(): Double {
        return 27.5
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }

    override fun castSoundId(): Int {
        return 216
    }

    override fun impactSoundId(): Int {
        return castSoundId() + 1
    }
}
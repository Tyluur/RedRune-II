package plugin.interaction.combat.magic.modern.wind

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/8/2017
 */
class WindStrikeSpellPlugin : CombatSpellPlugin {
    override fun spellId(): Int {
        return 25
    }

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
        return 20
    }

    override fun exp(): Double {
        return 5.5
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
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

    override fun castSoundId(): Int {
        return 220
    }

    override fun impactSoundId(): Int {
        return castSoundId() + 1
    }
}
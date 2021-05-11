package plugin.interaction.combat.magic.modern.earth

import game.content.entity.actor.combat.player.style.MagicCombatStyle
import game.content.plugin.combat.spell.type.CombatSpellPlugin
import game.entity.actor.Actor
import game.entity.actor.mask.Graphics
import game.entity.actor.player.Player
import game.entity.projectile.ProjectileManager
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/7/2017
 */
class EarthBlastSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 5
    }

    override fun animationId(): Int {
        return 14222
    }

    override fun hitGfx(): Int {
        return 2725
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 150
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        source.setNextGraphics(GRAPHICS)
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                2720,
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
        return 58
    }

    override fun exp(): Double {
        return 31.5
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }

    override fun castSoundId(): Int {
        return 128
    }

    override fun impactSoundId(): Int {
        return castSoundId() + 1
    }

    companion object {
        private val GRAPHICS = Graphics(2715)
    }
}
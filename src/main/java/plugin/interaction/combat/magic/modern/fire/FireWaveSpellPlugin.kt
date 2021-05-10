package plugin.interaction.combat.magic.modern.fire

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/7/2017
 */
class FireWaveSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 5
    }

    override fun animationId(): Int {
        return 14223
    }

    override fun hitGfx(): Int {
        return 2740
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 200
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        source.setNextGraphics(GRAPHICS)
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                2735,
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
        return 80
    }

    override fun exp(): Double {
        return 42.5
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }

    override fun castSoundId(): Int {
        return 162
    }

    override fun impactSoundId(): Int {
        return castSoundId() + 1
    }

    companion object {
        private val GRAPHICS = Graphics(2728)
    }
}
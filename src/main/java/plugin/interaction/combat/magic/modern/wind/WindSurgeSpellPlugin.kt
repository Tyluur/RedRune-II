package plugin.interaction.combat.magic.modern.wind

import game.content.plugin.combat.spell.type.CombatSpellPlugin
import game.entity.actor.Actor
import game.content.entity.actor.combat.player.style.MagicCombatStyle
import utility.constants.MagicConstants.MagicBook
import game.entity.projectile.ProjectileManager
import game.entity.actor.mask.Graphics
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/7/2017
 */
class WindSurgeSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 5
    }

    override fun animationId(): Int {
        return 10546
    }

    override fun hitGfx(): Int {
        return 2700
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 220
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        source.setNextGraphics(GRAPHICS)
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                462,
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
        return 84
    }

    override fun exp(): Double {
        return 80.0
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }

    companion object {
        private val GRAPHICS = Graphics(457)
    }
}
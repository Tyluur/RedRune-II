package plugin.interaction.combat.magic.modern.curse

import game.content.plugin.combat.spell.type.CombatSpellPlugin
import game.entity.actor.Actor
import game.content.entity.actor.combat.player.style.MagicCombatStyle
import utility.constants.MagicConstants.MagicBook
import game.entity.projectile.ProjectileManager
import game.entity.actor.mask.Graphics
import game.entity.actor.player.Player
import java.util.concurrent.TimeUnit

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/7/2017
 */
class EntangleSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 5
    }

    override fun animationId(): Int {
        return 710
    }

    override fun hitGfx(): Int {
        return -1
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 50
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        val freezeDelayed = target.freezeDelayed()
        val frozenTarget = target.isFrozen
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                178,
                18,
                9,
                52,
                15,
                0
            )
        )
        style.sendSpell(source, target, this, {
            if (frozenTarget || freezeDelayed) {
                return@sendSpell
            }
            // we send the graphics here because we don't always freeze them
            target.setNextGraphics(GRAPHICS)
            // only freeze the player if they are unfreezeable when the spell is cast.
            target.freeze(source, TimeUnit.SECONDS.toMillis(15), "You have been frozen!")
        }, null)
    }

    override fun spellId(): Int {
        return 81
    }

    override fun exp(): Double {
        return 91.1
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }

    companion object {
        private val GRAPHICS = Graphics(179)
    }
}
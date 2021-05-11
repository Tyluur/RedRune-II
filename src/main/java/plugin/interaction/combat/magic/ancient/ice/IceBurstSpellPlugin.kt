package plugin.interaction.combat.magic.ancient.ice

import game.content.entity.actor.combat.player.style.MagicCombatStyle
import game.content.plugin.combat.spell.type.CombatSpellPlugin
import game.entity.actor.Actor
import game.entity.actor.player.Player
import game.entity.projectile.ProjectileManager
import utility.constants.MagicConstants.MagicBook
import java.util.concurrent.TimeUnit

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/1/2017
 */
class IceBurstSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 1979
    }

    override fun hitGfx(): Int {
        return 363
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 220
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        val freezeDelayed = target.freezeDelayed()
        val frozenTarget = target.isFrozen
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                366,
                43,
                21,
                52,
                15,
                0
            )
        )
        style.sendMultiSpell(source, target, this, {
            if (frozenTarget || freezeDelayed) {
                return@sendMultiSpell
            }
            // only freeze the player if they are unfreezeable when the spell is cast.
            target.freeze(source, TimeUnit.SECONDS.toMillis(10), "You have been frozen!")
        }, null)
    }

    override fun gfxHeight(): Int {
        return 0
    }

    override fun spellId(): Int {
        return 22
    }

    override fun exp(): Double {
        return 46.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }

    override fun castSoundId(): Int {
        return 171
    }

    override fun impactSoundId(): Int {
        return 170
    }
}
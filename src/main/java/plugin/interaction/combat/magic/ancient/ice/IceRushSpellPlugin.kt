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
class IceRushSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 1978
    }

    override fun hitGfx(): Int {
        return 389
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 190
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        val freezeDelayed = target.freezeDelayed()
        val frozenTarget = target.isFrozen
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                362,
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
            // only freeze the player if they are unfreezeable when the spell is cast.
            target.freeze(source, TimeUnit.SECONDS.toMillis(5), "You have been frozen!")
        }, null)
    }

    override fun castSoundId(): Int {
        return 171
    }

    override fun impactSoundId(): Int {
        return 173
    }

    override fun spellId(): Int {
        return 20
    }

    override fun exp(): Double {
        return 36.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }
}
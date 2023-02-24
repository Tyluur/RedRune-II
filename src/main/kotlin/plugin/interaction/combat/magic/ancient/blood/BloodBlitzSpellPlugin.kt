package plugin.interaction.combat.magic.ancient.blood

import org.redrune.game.content.entity.actor.combat.CombatSwingDetail
import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/1/2017
 */
class BloodBlitzSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 1978
    }

    override fun hitGfx(): Int {
        return 375
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 250
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                374,
                18,
                9,
                52,
                15,
                0
            )
        )
        style.sendSpell(source, target, this, null, null).consume { detail: CombatSwingDetail ->
            if (detail.hit.damage != 0) {
                source.packets.sendMessage("You drain some of your opponents' life points.")
                source.heal(detail.hit.damage / 4)
            }
        }
    }

    override fun spellId(): Int {
        return 25
    }

    override fun exp(): Double {
        return 45.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }
}
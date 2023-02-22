package plugin.interaction.combat.magic.modern.special

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.utility.constants.MagicConstants.MagicBook
import java.util.concurrent.TimeUnit

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/7/2017
 */
class TeleblockSpellPlugin : CombatSpellPlugin {
    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 10503
    }

    override fun hitGfx(): Int {
        return 1843
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 30
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        if (target is NPC) {
            source.packets.sendMessage("You cannot cast teleport block on monsters.")
            return
        }
        val p2 = target as Player
        val hasImmunity = target.hasTeleblockImmunity()
        val isTeleblocked = target.isTeleblocked()
        if (hasImmunity || isTeleblocked) {
            source.packets.sendMessage("This player is already affected by teleport block.")
            return
        }
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                1842,
                18,
                9,
                52,
                15,
                0
            )
        )
        style.sendSpell(source, target, this, {
            target.setNextGraphics(GRAPHICS)
            //TODO: get protect from magic prayers
            val teleblockTime = if (p2.prayer.usingPrayer(0, 0)) 150 else 300
            target.teleblock(source, TimeUnit.SECONDS.toMillis(teleblockTime.toLong()))
        }, null)
    }

    override fun spellId(): Int {
        return 86
    }

    override fun exp(): Double {
        return 80.0
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }

    companion object {
        private val GRAPHICS = Graphics(1841)
    }
}
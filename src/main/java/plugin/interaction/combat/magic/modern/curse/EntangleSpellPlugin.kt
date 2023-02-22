package plugin.interaction.combat.magic.modern.curse

import org.redrune.engine.cycle.GameCycleWorker.Companion.ticksPassed
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle
import plugin.interaction.combat.magic.modern.god.ChargeSpellPlugin
import org.redrune.engine.cycle.GameCycleWorker
import org.redrune.utility.constants.MagicConstants.MagicBook
import org.redrune.game.entity.actor.mask.Animation
import plugin.interaction.combat.magic.modern.god.StormOfArmadylSpellPlugin
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.utility.constants.SkillConstants
import plugin.interaction.combat.magic.modern.fire.FireBoltSpellPlugin
import plugin.interaction.combat.magic.modern.fire.FireWaveSpellPlugin
import plugin.interaction.combat.magic.modern.fire.FireBlastSpellPlugin
import plugin.interaction.combat.magic.modern.fire.FireSurgeSpellPlugin
import plugin.interaction.combat.magic.modern.fire.FireStrikeSpellPlugin
import plugin.interaction.combat.magic.modern.wind.WindSurgeSpellPlugin
import plugin.interaction.combat.magic.modern.curse.BindSpellPlugin
import plugin.interaction.combat.magic.modern.curse.StunSpellPlugin
import plugin.interaction.combat.magic.modern.curse.CurseSpellPlugin
import plugin.interaction.combat.magic.modern.curse.SnareSpellPlugin
import plugin.interaction.combat.magic.modern.curse.WeakenSpellPlugin
import plugin.interaction.combat.magic.modern.curse.EnfeebleSpellPlugin
import plugin.interaction.combat.magic.modern.curse.EntangleSpellPlugin
import plugin.interaction.combat.magic.modern.curse.VulernabilitySpellPlugin
import plugin.interaction.combat.magic.modern.earth.EarthBoltSpellPlugin
import plugin.interaction.combat.magic.modern.earth.EarthWaveSpellPlugin
import plugin.interaction.combat.magic.modern.earth.EarthBlastSpellPlugin
import plugin.interaction.combat.magic.modern.earth.EarthSurgeSpellPlugin
import plugin.interaction.combat.magic.modern.earth.EarthStrikeSpellPlugin
import plugin.interaction.combat.magic.modern.water.WaterBoltSpellPlugin
import plugin.interaction.combat.magic.modern.water.WaterWaveSpellPlugin
import plugin.interaction.combat.magic.modern.water.WaterBlastSpellPlugin
import plugin.interaction.combat.magic.modern.water.WaterSurgeSpellPlugin
import plugin.interaction.combat.magic.modern.water.WaterStrikeSpellPlugin
import org.redrune.game.entity.actor.npc.NPC
import plugin.interaction.combat.magic.modern.special.TeleblockSpellPlugin
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.GameConstants
import org.redrune.utility.constants.MagicConstants
import java.util.concurrent.TimeUnit

/**
 * @author Tyluur <itstyluur@icloud.com>
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
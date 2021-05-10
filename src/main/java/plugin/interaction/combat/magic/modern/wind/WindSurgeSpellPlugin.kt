package plugin.interaction.combat.magic.modern.wind

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
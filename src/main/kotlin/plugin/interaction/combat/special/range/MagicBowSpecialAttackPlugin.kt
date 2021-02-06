package plugin.interaction.combat.special.range

import org.redrune.game.content.plugin.combat.SpecialAttackPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import plugin.interaction.combat.special.magic.StaffOfLightSpecialAttackPlugin
import org.redrune.game.entity.actor.mask.Animation
import plugin.interaction.combat.special.melee.DragonHalberdSpecialPlugin
import plugin.interaction.combat.special.melee.ExcaliburSpecialAttackPlugin
import org.redrune.game.entity.actor.player.data.PlayerSkills
import org.redrune.utility.constants.SkillConstants
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.engine.tick.task.WorldTask
import org.redrune.game.entity.actor.mask.ForceTalk
import org.redrune.utility.constants.GameConstants
import plugin.interaction.combat.special.melee.DragonClawSpecialAttackPlugin
import org.redrune.utility.functions.Misc
import plugin.interaction.combat.special.melee.DragonMaceSpecialAttackPlugin
import plugin.interaction.combat.special.melee.AbyssalWhipSpecialAttackPlugin
import plugin.interaction.combat.special.melee.GraniteMaulSpecialAttackPlugin
import plugin.interaction.combat.special.melee.KorasiSwordSpecialAttackPlugin
import org.redrune.game.entity.actor.mask.HitSplat
import plugin.interaction.combat.special.melee.DragonDaggerSpecialAttackPlugin
import plugin.interaction.combat.special.melee.BandosGodswordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.DragonScimitarSpecialAttackPlugin
import plugin.interaction.combat.special.melee.SaradominSwordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.VestaLongswordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.ArmadylGodswordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.DragonBattleaxeSpecialAttackPlugin
import plugin.interaction.combat.special.melee.DragonLongswordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.ZamorakGodswordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.StatiusWarhammerSpecialAttackPlugin
import plugin.interaction.combat.special.melee.BarrelchestAnchorSpecialAttackPlugin
import plugin.interaction.combat.special.melee.SaradominGodswordSpecialAttackPlugin
import plugin.interaction.combat.special.range.MagicBowSpecialAttackPlugin
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin
import org.redrune.game.entity.projectile.Projectile
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.combat.CombatSwingDetail
import plugin.interaction.combat.special.range.HandCannonSpecialAttackPlugin
import plugin.interaction.combat.special.range.MorriganJavelinSpecialAttackPlugin
import org.redrune.game.entity.actor.mask.Hit
import org.redrune.game.entity.actor.player.Player
import plugin.interaction.combat.special.range.MorriganThrownAxeSpecialAttackPlugin

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/6/2017
 */
class MagicBowSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(859, 861, 10284, 18332)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.0),
            style.getRandomDamage(source, target, 1.0),
            ProjectileManager.getProjectileDelay(source, target)
        )
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.0),
            style.getRandomDamage(source, target, 1.0),
            ProjectileManager.getProjectileDelay(source, target)
        )
        visualize(source, target)
        RangeWeaponPlugin.dropAmmo(source, target, 1)
        RangeWeaponPlugin.dropAmmo(source, target, 1)
    }

    /**
     * Visualizes the projectiles
     *
     * @param source
     * The projectile from
     * @param target
     * The projectile to
     */
    private fun visualize(source: Player, target: Actor) {
        var speed = (27.0 + source.getDistance(target) * 5.0).toInt()
        ProjectileManager.sendProjectile(Projectile(source, target, 249, 41, 36, 20, speed, 15, 0))
        speed = (20.0 + source.getDistance(target) * 10.0).toInt()
        ProjectileManager.sendProjectile(Projectile(source, target, 249, 41, 36, 40, speed, 10, 0))
    }

    companion object {
        private val ANIMATION = Animation(1074)
    }
}
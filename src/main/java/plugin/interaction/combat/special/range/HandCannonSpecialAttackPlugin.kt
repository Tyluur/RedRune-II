package plugin.interaction.combat.special.range

import org.redrune.game.content.plugin.combat.SpecialAttackPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import plugin.interaction.combat.special.magic.StaffOfLightSpecialAttackPlugin
import plugin.interaction.combat.special.melee.DragonHalberdSpecialPlugin
import plugin.interaction.combat.special.melee.ExcaliburSpecialAttackPlugin
import org.redrune.game.entity.actor.player.data.PlayerSkills
import org.redrune.utility.constants.SkillConstants
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.engine.tick.task.WorldTask
import org.redrune.utility.constants.GameConstants
import plugin.interaction.combat.special.melee.DragonClawSpecialAttackPlugin
import org.redrune.utility.functions.Misc
import plugin.interaction.combat.special.melee.DragonMaceSpecialAttackPlugin
import plugin.interaction.combat.special.melee.AbyssalWhipSpecialAttackPlugin
import plugin.interaction.combat.special.melee.GraniteMaulSpecialAttackPlugin
import plugin.interaction.combat.special.melee.KorasiSwordSpecialAttackPlugin
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
import org.redrune.game.entity.actor.mask.*
import plugin.interaction.combat.special.range.HandCannonSpecialAttackPlugin
import plugin.interaction.combat.special.range.MorriganJavelinSpecialAttackPlugin
import org.redrune.game.entity.actor.player.Player
import plugin.interaction.combat.special.range.MorriganThrownAxeSpecialAttackPlugin

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/6/2017
 */
class HandCannonSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(15241)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = Animation(12175)
        WorldTasksManager.schedule(object : WorldTask() {
            var loop = 0
            override fun run() {
                if (target.isDead || source.isDead || loop > 1) {
                    stop()
                    return
                }
                if (loop == 0) {
                    source.nextAnimation = FIRE_ANIMATION
                    source.setNextGraphics(GRAPHICS)
                    ProjectileManager.sendProjectile(
                        ProjectileManager.createSpeedDefinedProjectile(
                            source,
                            target,
                            2143,
                            18,
                            36,
                            41,
                            5,
                            0
                        )
                    )
                    style.sendHit(
                        source,
                        target,
                        style.calculator.getMaximumHit(source, 1.05),
                        style.getRandomDamage(source, target, 1.05),
                        ProjectileManager.getProjectileDelay(source, target)
                    )
                } else if (loop == 1) {
                    source.nextAnimation = FIRE_ANIMATION
                    source.setNextGraphics(GRAPHICS)
                    ProjectileManager.sendProjectile(
                        ProjectileManager.createSpeedDefinedProjectile(
                            source,
                            target,
                            2143,
                            18,
                            36,
                            41,
                            5,
                            0
                        )
                    )
                    style.sendHit(
                        source,
                        target,
                        style.calculator.getMaximumHit(source, 1.05),
                        style.getRandomDamage(source, target, 1.05),
                        ProjectileManager.getProjectileDelay(source, target)
                    )
                    stop()
                }
                loop++
            }
        }, 0, 0.25.toInt())
    }

    companion object {
        private val GRAPHICS = Graphics(2138)
        private val FIRE_ANIMATION = Animation(12174)
    }
}
package plugin.interaction.combat.special.range

import org.redrune.engine.tick.task.WorldTask
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.ProjectileManager

/**
 * @author Tyluur <itstyluur@icloud.com>
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
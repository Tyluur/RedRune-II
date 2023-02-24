package plugin.interaction.combat.special.range

import org.redrune.engine.tick.task.WorldTask
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.mask.Hit
import org.redrune.game.entity.actor.mask.HitSplat
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.Projectile
import org.redrune.game.entity.projectile.ProjectileManager

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/13/2017
 */
class MorriganJavelinSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(13879, 13880, 13881, 13882)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.setNextGraphics(GRAPHICS)
        source.nextAnimation = ANIMATION
        val randomDamage = style.getRandomDamage(source, target, 1.0)
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.0),
            randomDamage,
            ProjectileManager.getProjectileDelay(source, target)
        )
        ProjectileManager.sendProjectile(
            Projectile(
                source,
                target,
                1837,
                41,
                36,
                20,
                (27.0 + source.getDistance(target) * 5.0).toInt(),
                0,
                0
            )
        )
        RangeWeaponPlugin.dropAmmo(source, target, -1)
        RangeWeaponPlugin.dropAmmo(source, target, -1)
        fireSpecialModifier(source, target, randomDamage)
    }

    private fun fireSpecialModifier(source: Player, target: Actor, randomDamage: Int) {
        if (randomDamage > 0) {
            WorldTasksManager.schedule(object : WorldTask() {
                var damage = randomDamage
                override fun run() {
                    if (target.isDead || target.isFinished) {
                        stop()
                        return
                    }
                    if (damage > 50) {
                        damage -= 50
                        if (!target.getTemporaryAttribute("teleporting", false)) {
                            target.applyHit(Hit(source, 50, HitSplat.REGULAR_DAMAGE))
                        }
                    } else {
                        if (!target.getTemporaryAttribute("teleporting", false)) {
                            target.applyHit(Hit(source, damage, HitSplat.REGULAR_DAMAGE))
                        }
                        stop()
                    }
                }
            }, 4, 2)
        }
    }

    companion object {
        private val ANIMATION = Animation(10501)
        private val GRAPHICS = Graphics(1836)
    }
}
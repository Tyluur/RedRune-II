package plugin.interaction.combat.special.range

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.Projectile
import org.redrune.game.entity.projectile.ProjectileManager

/**
 * @author Tyluur <itstyluur@icloud.com>
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
package plugin.interaction.combat.special.range

import game.content.plugin.combat.SpecialAttackPlugin
import game.entity.actor.Actor
import game.content.entity.actor.combat.player.AbstractCombatStyle
import game.entity.actor.mask.Animation
import game.entity.projectile.ProjectileManager
import game.content.plugin.combat.RangeWeaponPlugin
import game.entity.projectile.Projectile
import game.entity.actor.player.Player

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
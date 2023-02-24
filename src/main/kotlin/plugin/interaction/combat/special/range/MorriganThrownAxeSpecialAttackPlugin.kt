package plugin.interaction.combat.special.range

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.Projectile
import org.redrune.game.entity.projectile.ProjectileManager

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/13/2017
 */
class MorriganThrownAxeSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(13883, 13957)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.setNextGraphics(GRAPHICS)
        source.nextAnimation = ANIMATION
        ProjectileManager.sendProjectile(
            Projectile(
                source,
                target,
                1839,
                29,
                15,
                20,
                (27.0 + source.getDistance(target) * 5.0).toInt(),
                0,
                0
            )
        )
        val randomDamage = style.getRandomDamage(source, target, 1.0)
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.0),
            randomDamage,
            ProjectileManager.getProjectileDelay(source, target)
        )
        RangeWeaponPlugin.dropAmmo(source, target, -1)
    }

    companion object {
        private val ANIMATION = Animation(10504)
        private val GRAPHICS = Graphics(1838)
    }
}
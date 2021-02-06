package plugin.interaction.combat.range

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.ProjectileManager

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/6/2017
 */
class CrystalBowPlugin : RangeWeaponPlugin() {
    override fun getWeaponNames(): Array<String> {
        return arguments("crystal bow")
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                249,
                40,
                30,
                41,
                15,
                0
            )
        )
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.0),
            style.getRandomDamage(source, target, 1.0),
            ProjectileManager.getProjectileDelay(source, target)
        )
    }
}
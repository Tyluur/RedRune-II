package plugin.interaction.combat.range

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.Projectile
import org.redrune.game.entity.projectile.ProjectileManager

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/6/2017
 */
class DarkBowPlugin : RangeWeaponPlugin() {
    override fun getWeaponNames(): Array<String> {
        return arguments("dark bow")
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        val weaponId = source.equipment.weaponId
        val ammoId = source.equipment.ammoId
        val speed = 46 + source.getDistance(target) * 5
        val speed2 = 55 + source.getDistance(target) * 10
        source.setNextGraphics(Graphics(CombatAlgorithm.getArrowThrowGfxId(ammoId), 0, 100))
        for (i in 1..2) {
            style.sendHit(
                source,
                target,
                style.calculator.getMaximumHit(source, 1.0),
                style.getRandomDamage(source, target, 1.0),
                ProjectileManager.getProjectileDelay(source, target)
            )
            val projectile = Projectile(
                source,
                target,
                CombatAlgorithm.getArrowProjectileGfxId(weaponId, ammoId),
                41,
                35,
                41,
                if (i == 1) speed else speed2,
                if (i == 1) 5 else 25,
                0
            )
            ProjectileManager.sendProjectile(projectile)
            dropAmmo(source, target, 1)
        }
    }
}
package plugin.interaction.combat.range

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.mask.Hit
import org.redrune.game.entity.actor.mask.HitSplat
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.utility.constants.SkillConstants
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/6/2017
 */
class HandCannonPlugin : RangeWeaponPlugin() {
    override fun getWeaponNames(): Array<String> {
        return arguments("hand cannon")
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        if (Misc.getRandom(source.skills.getLevel(SkillConstants.FIREMAKING) shl 1) == 0) {
            source.setNextGraphics(EXPLODE_GRAPHICS)
            source.equipment.items[3] = null
            source.equipment.refresh(3)
            source.appearance.generateAppearanceData()
            source.applyHit(Hit(source, Misc.getRandom(150) + 10, HitSplat.REGULAR_DAMAGE))
        } else {
            source.setNextGraphics(FIRE_GRAPHICS)
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
                style.calculator.getMaximumHit(source, 1.0),
                style.getRandomDamage(source, target, 1.0),
                ProjectileManager.getProjectileDelay(source, target)
            )
            dropAmmo(source, target, -2)
        }
    }

    companion object {
        private val ANIMATION = Animation(12175)
        private val EXPLODE_GRAPHICS = Graphics(2140)
        private val FIRE_GRAPHICS = Graphics(2138)
    }
}
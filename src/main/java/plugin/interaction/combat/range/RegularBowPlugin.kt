package plugin.interaction.combat.range

import game.content.entity.actor.combat.CombatAlgorithm
import game.content.entity.actor.combat.player.AbstractCombatStyle
import game.content.plugin.combat.RangeWeaponPlugin
import game.entity.actor.Actor
import game.entity.actor.mask.Graphics
import game.entity.actor.player.Player
import game.entity.projectile.ProjectileManager

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/6/2017
 */
class RegularBowPlugin : RangeWeaponPlugin() {
    override fun getWeaponNames(): Array<String> {
        return arguments("shortbow", "* shortbow", "* longbow")
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        val weaponId = source.equipment.weaponId
        val ammoId = source.equipment.ammoId
        source.setNextGraphics(Graphics(CombatAlgorithm.getArrowThrowGfxId(ammoId), 0, 100))
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                CombatAlgorithm.getArrowProjectileGfxId(weaponId, ammoId),
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
        dropAmmo(source, target, 1)
    }
}
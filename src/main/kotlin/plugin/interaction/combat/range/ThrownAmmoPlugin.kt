package plugin.interaction.combat.range

import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.Projectile
import org.redrune.game.entity.projectile.ProjectileManager
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/6/2017
 */
class ThrownAmmoPlugin : RangeWeaponPlugin() {
    override fun getWeaponNames(): Array<String> {
        return arguments("toktz-xil-ul", "* throwing axe", "* thrownaxe", "* javelin", "* dart", "* knife")
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        val weaponId = source.equipment.weaponId
        val name = ItemDefinitions.getItemDefinitions(weaponId).name.lowercase(Locale.getDefault())
        if (!name.contains("javelin") && !name.contains("throwing axe") && !name.contains("thrownaxe") && !name.contains(
                "toktz"
            )
        ) {
            source.setNextGraphics(Graphics(CombatAlgorithm.getKnifeThrowGfxId(weaponId), 0, 96))
        }
        // diff. projectile types
        if (name.contains("knife")) {
            val speed = 46 + ProjectileManager.getLocation(source).getDistance(target) * 5
            ProjectileManager.sendProjectile(
                Projectile(
                    source,
                    target,
                    CombatAlgorithm.getKnifeThrowGfxId(weaponId),
                    30,
                    26,
                    32,
                    speed,
                    15,
                    1
                )
            )
        } else if (name.contains("dart") || name.contains("toktz")) {
            ProjectileManager.sendProjectile(
                ProjectileManager.createSpeedDefinedProjectile(
                    source,
                    target,
                    CombatAlgorithm.getKnifeThrowGfxId(weaponId),
                    40,
                    36,
                    32,
                    15,
                    0
                )
            )
        } else if (name.contains("throwing axe")) {
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
        }
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.0),
            style.getRandomDamage(source, target, 1.0),
            ProjectileManager.getProjectileDelay(source, target)
        )
        dropAmmo(source, target, -1)
    }
}
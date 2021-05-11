package plugin.interaction.combat.special.range

import game.content.plugin.combat.SpecialAttackPlugin
import game.entity.actor.Actor
import game.content.entity.actor.combat.player.AbstractCombatStyle
import game.entity.projectile.ProjectileManager
import game.content.plugin.combat.RangeWeaponPlugin
import game.entity.projectile.Projectile
import game.content.entity.actor.combat.CombatAlgorithm
import game.content.entity.actor.combat.CombatSwingDetail
import game.entity.actor.mask.*
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/6/2017
 */
class DragonBowSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(11235, 13405, 15701, 15702, 15703, 15704)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        val arrowId = source.equipment.ammoId
        val weaponId = source.equipment.weaponId
        val attackStyle = source.combatDefinitions.attackStyle

        // animates
        source.nextAnimation = Animation(CombatAlgorithm.getWeaponAttackEmote(weaponId, attackStyle))
        val delay = ProjectileManager.getProjectileDelay(source, target)
        var damage: Int
        var damage2: Int
        val maxHit: Int
        var dragon: Boolean
        if (arrowId == 11212) {
            maxHit = style.calculator.getMaximumHit(source, 1.0)
            damage = style.getRandomDamage(source, target, 1.5)
            damage2 = style.getRandomDamage(source, target, 1.5)
            if (damage < 80) {
                damage = 80
            }
            if (damage2 < 80) {
                damage2 = 80
            }
            visualize(source, target, true.also { dragon = it })
        } else {
            maxHit = style.calculator.getMaximumHit(source, 1.0)
            damage = style.getRandomDamage(source, target, 1.3)
            damage2 = style.getRandomDamage(source, target, 1.3)
            if (damage < 50) {
                damage = 50
            }
            if (damage2 < 50) {
                damage2 = 50
            }
            visualize(source, target, false.also { dragon = it })
        }
        val dragonAmmo = dragon
        style.sendHit(source, target, maxHit, damage, delay - 1).consume { detail: CombatSwingDetail ->
            detail.hit.setLandTask {
                target.setNextGraphics(
                    Graphics(if (dragonAmmo) 1100 else 1103, 0, 100)
                )
            }
        }
        style.sendHit(source, target, maxHit, damage2, delay)

        // drops the ammo
        RangeWeaponPlugin.dropAmmo(source, target, 1)
        RangeWeaponPlugin.dropAmmo(source, target, 1)
    }

    /**
     * Visualizes the dark bow projectiles
     *
     * @param player
     * The player
     * @param target
     * The target
     * @param dragon
     * If we should use the dragon projectile
     */
    fun visualize(player: Player?, target: Actor?, dragon: Boolean) {
        val projectileId = if (dragon) 1099 else 1101
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                player,
                target,
                projectileId,
                40,
                36,
                46,
                5,
                0
            )
        )
        val speed = ProjectileManager.getSpeedModifier(player, target)
        ProjectileManager.sendProjectile(Projectile(player, target, projectileId, 40, 36, 51, speed + 10, 25, 0))
    }
}
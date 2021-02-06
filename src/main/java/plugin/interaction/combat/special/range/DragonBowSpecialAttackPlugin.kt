package plugin.interaction.combat.special.range

import org.redrune.game.content.plugin.combat.SpecialAttackPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import plugin.interaction.combat.special.magic.StaffOfLightSpecialAttackPlugin
import plugin.interaction.combat.special.melee.DragonHalberdSpecialPlugin
import plugin.interaction.combat.special.melee.ExcaliburSpecialAttackPlugin
import org.redrune.game.entity.actor.player.data.PlayerSkills
import org.redrune.utility.constants.SkillConstants
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.engine.tick.task.WorldTask
import org.redrune.utility.constants.GameConstants
import plugin.interaction.combat.special.melee.DragonClawSpecialAttackPlugin
import org.redrune.utility.functions.Misc
import plugin.interaction.combat.special.melee.DragonMaceSpecialAttackPlugin
import plugin.interaction.combat.special.melee.AbyssalWhipSpecialAttackPlugin
import plugin.interaction.combat.special.melee.GraniteMaulSpecialAttackPlugin
import plugin.interaction.combat.special.melee.KorasiSwordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.DragonDaggerSpecialAttackPlugin
import plugin.interaction.combat.special.melee.BandosGodswordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.DragonScimitarSpecialAttackPlugin
import plugin.interaction.combat.special.melee.SaradominSwordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.VestaLongswordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.ArmadylGodswordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.DragonBattleaxeSpecialAttackPlugin
import plugin.interaction.combat.special.melee.DragonLongswordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.ZamorakGodswordSpecialAttackPlugin
import plugin.interaction.combat.special.melee.StatiusWarhammerSpecialAttackPlugin
import plugin.interaction.combat.special.melee.BarrelchestAnchorSpecialAttackPlugin
import plugin.interaction.combat.special.melee.SaradominGodswordSpecialAttackPlugin
import plugin.interaction.combat.special.range.MagicBowSpecialAttackPlugin
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin
import org.redrune.game.entity.projectile.Projectile
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.combat.CombatSwingDetail
import org.redrune.game.entity.actor.mask.*
import plugin.interaction.combat.special.range.HandCannonSpecialAttackPlugin
import plugin.interaction.combat.special.range.MorriganJavelinSpecialAttackPlugin
import org.redrune.game.entity.actor.player.Player
import plugin.interaction.combat.special.range.MorriganThrownAxeSpecialAttackPlugin

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
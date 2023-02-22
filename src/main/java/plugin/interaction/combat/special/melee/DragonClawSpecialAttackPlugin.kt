package plugin.interaction.combat.special.melee

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
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/5/2017
 */
class DragonClawSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(14484, 23695)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        var hits = intArrayOf(0, 1)
        var hit = style.getRandomDamage(source, target, 1.0)
        run {
            var i = 20
            while (i <= 80) {
                // all 4 d claw specs in right timing
                source.packets.sendSound(7464, i, 1)
                i += 20
            }
        }
        if (hit > 100) {
            hits = intArrayOf(hit, hit / 2, hit / 2 / 2, hit / 2 - hit / 2 / 2)
        } else {
            hit = style.getRandomDamage(source, target, 1.0)
            if (hit > 100) {
                hits = intArrayOf(0, hit, hit / 2, hit - hit / 2)
            } else {
                hit = style.getRandomDamage(source, target, 1.0)
                if (hit > 100) {
                    hits = intArrayOf(0, 0, hit / 2, hit / 2 + 10)
                } else {
                    hit = style.getRandomDamage(source, target, 1.0)
                    if (hit > 100) {
                        hits = intArrayOf(0, 0, 0, hit)
                    } else {
                        val miss = intArrayOf(Misc.random(10), Misc.random(10))
                        for (i in miss.indices) {
                            style.sendHit(source, target, style.calculator.getMaximumHit(source, 1.0), miss[i], 0)
                        }
                    }
                }
            }
        }
        for (i in hits.indices) {
            if (i > 1) {
                style.sendHit(source, target, style.calculator.getMaximumHit(source, 1.0), hits[i], 1)
            } else {
                style.sendHit(source, target, style.calculator.getMaximumHit(source, 1.0), hits[i], 0)
            }
        }
    }

    companion object {
        private val ANIMATION = Animation(10961)
        private val GRAPHICS = Graphics(1950)
    }
}
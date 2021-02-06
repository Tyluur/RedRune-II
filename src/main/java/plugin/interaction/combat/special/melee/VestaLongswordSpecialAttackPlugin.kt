package plugin.interaction.combat.special.melee

import org.redrune.game.content.plugin.combat.SpecialAttackPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import plugin.interaction.combat.special.magic.StaffOfLightSpecialAttackPlugin
import org.redrune.game.entity.actor.mask.Animation
import plugin.interaction.combat.special.melee.DragonHalberdSpecialPlugin
import plugin.interaction.combat.special.melee.ExcaliburSpecialAttackPlugin
import org.redrune.game.entity.actor.player.data.PlayerSkills
import org.redrune.utility.constants.SkillConstants
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.engine.tick.task.WorldTask
import org.redrune.game.entity.actor.mask.ForceTalk
import org.redrune.utility.constants.GameConstants
import plugin.interaction.combat.special.melee.DragonClawSpecialAttackPlugin
import org.redrune.utility.functions.Misc
import plugin.interaction.combat.special.melee.DragonMaceSpecialAttackPlugin
import plugin.interaction.combat.special.melee.AbyssalWhipSpecialAttackPlugin
import plugin.interaction.combat.special.melee.GraniteMaulSpecialAttackPlugin
import plugin.interaction.combat.special.melee.KorasiSwordSpecialAttackPlugin
import org.redrune.game.entity.actor.mask.HitSplat
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
import plugin.interaction.combat.special.range.HandCannonSpecialAttackPlugin
import plugin.interaction.combat.special.range.MorriganJavelinSpecialAttackPlugin
import org.redrune.game.entity.actor.mask.Hit
import org.redrune.game.entity.actor.player.Player
import plugin.interaction.combat.special.range.MorriganThrownAxeSpecialAttackPlugin

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/5/2017
 */
class VestaLongswordSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(13899, 13901)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.20),
            style.getRandomDamage(source, target, 1.20),
            0
        )
    }

    companion object {
        private val ANIMATION = Animation(10502)
    }
}
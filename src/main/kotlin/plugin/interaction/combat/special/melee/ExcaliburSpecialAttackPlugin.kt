package plugin.interaction.combat.special.melee

import org.redrune.engine.tick.task.WorldTask
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.game.GameFlags
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.ForceTalk
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/5/2017
 */
class ExcaliburSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(35, 8280, 14632)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        val weaponId = source.equipment.weaponId
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        source.nextForceTalk = FORCE_TALK
        val enhanced = weaponId == 14632
        val skills = source.skills
        skills[DEFENCE] = if (enhanced) (skills.getLevelForXp(DEFENCE) * 1.15).toInt() else skills.getLevel(DEFENCE) + 8
        WorldTasksManager.schedule(object : WorldTask() {
            var count = 5
            override fun run() {
                if (source.isDead || source.isFinished || source.hitpoints >= source.maxHitpoints) {
                    stop()
                    return
                }
                source.heal(if (enhanced) 80 else 40)
                if (count-- == 0) {
                    stop()
                }
            }
        }, 4, 2)
    }

    override fun isInstant(): Boolean {
        return true
    }

    override fun requiresFight(): Boolean {
        return false
    }

    companion object {
        private val ANIMATION = Animation(1168)
        private val GRAPHICS = Graphics(247)
        private val FORCE_TALK = ForceTalk("For ${GameFlags.SERVER_NAME.toUpperCase()}!")
    }
}
package plugin.interaction.combat.special.melee

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
class DragonBattleaxeSpecialAttackPlugin : SpecialAttackPlugin() {
    override fun getWeaponIds(): IntArray {
        return arguments(1377, 13472)
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        source.nextAnimation = ANIMATION
        source.setNextGraphics(GRAPHICS)
        source.nextForceTalk = ForceTalk("Raarrrrrgggggghhhhhhh!")
        val skills = source.skills
        val defence = (skills.getLevel(DEFENCE) * 0.90).toInt()
        val attack = (skills.getLevel(ATTACK) * 0.90).toInt()
        val range = (skills.getLevel(RANGE) * 0.90).toInt()
        val magic = (skills.getLevel(MAGIC) * 0.90).toInt()
        val strength = (skills.getLevel(STRENGTH) * 1.2).toInt()
        skills[DEFENCE] = defence
        skills[ATTACK] = attack
        skills[RANGE] = range
        skills[MAGIC] = magic
        skills[STRENGTH] = strength
    }

    override fun isInstant(): Boolean {
        return true
    }

    override fun requiresFight(): Boolean {
        return false
    }

    companion object {
        private val GRAPHICS = Graphics(246)
        private val ANIMATION = Animation(1056)
    }
}
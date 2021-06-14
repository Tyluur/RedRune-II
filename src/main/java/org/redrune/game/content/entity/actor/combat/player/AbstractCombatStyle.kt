package org.redrune.game.content.entity.actor.combat.player

import org.redrune.game.content.entity.actor.combat.CombatSwingDetail
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Hit
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.map.region.RegionManager
import org.redrune.utility.constants.SkillConstants
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
abstract class AbstractCombatStyle
/**
 * Constructs a new combat style enumeration instance
 */ protected constructor(
    /**
     * The combat style calculator
     */
    val calculator: AbstractCombatCalculator
) : SkillConstants {
    /**
     * This method handles the swing of a combat style
     */
    abstract fun fireSwing(source: Player, target: Actor): Boolean

    /**
     * Handles the addition of experience
     */
    abstract fun addExperience(source: Player, target: Actor, hit: Hit, attackStyle: Int, weaponId: Int)

    /**
     * Gets the random damage, based on the calculated maximum values
     *
     * @param source The source
     */
    abstract fun getRandomDamage(source: Actor, target: Actor, multiplier: Double): Int

    /**
     * Sends the hit to the target
     */
    abstract fun sendHit(source: Player, target: Actor, maxHit: Int, damage: Int, delay: Int): CombatSwingDetail?

    /**
     * Plays a sound to a single player
     */
    fun playSingleSound(player: Player, soundId: Int) {
        if (soundId == -1) {
            return
        }
        player.packets.sendSound(soundId, 0, 1)
    }

    /**
     * Plays a sound to all players in the source's area, within a 5 tile radius
     */
    fun playAreaSound(player: Player, soundId: Int) {
        if (soundId == -1) {
            return
        }
        val region = RegionManager.getRegion(player.regionId) ?: return
        for (p in region.getPlayersWithinDistance(player, 5)) {
            if (p == null) {
                continue
            }
            p.packets.sendSound(soundId, 0, 1)
        }
    }

    /**
     * Handles effects like protection prayers, soulsplit, spirit shields etc.
     */
    fun handleEffects(source: Player, target: Actor, hit: Hit) {
        if (target.isPlayer) {
            val p2 = target as Player
            val shieldId = p2.equipment.shieldId
            if (shieldId == 13740) { //divine
                val drain = (Math.ceil(hit.damage * 0.3) / 2).toInt()
                if (p2.prayer.prayerpoints >= drain) {
                    hit.damage = (hit.damage * 0.70).toInt()
                    p2.prayer.drainPrayer(drain)
                }
            }
            if (Misc.getRandom(100) <= 70) { //elysian
                hit.damage = (hit.damage * 0.75).toInt()
            }
            if (p2.prayer.hasPrayersOn() && hit.damage != 0) {
                p2.prayer.handleCombatDeflection(source, hit)
            }
            if (hit.damage >= 200) {
                source.combatDefinitions.handleSoaking(source, target, hit)
            }
            if (p2.attributes.polDelay > Misc.currentTimeMillis()) {
                hit.damage = (hit.damage * 0.5).toInt()
            }
        }
        source.prayer.handleCurseBoosts(target, hit)
    }
}
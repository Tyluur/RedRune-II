package plugin.interaction.combat.magic.lunar

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.plugin.combat.spell.type.RegularSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import org.redrune.game.global.map.region.RegionManager
import org.redrune.utility.constants.MagicConstants
import org.redrune.utility.constants.MagicConstants.MagicBook
import org.redrune.utility.constants.SkillConstants
import java.util.concurrent.TimeUnit

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/27/2017
 */
class VengeanceGroupSpell : RegularSpellPlugin {
    override fun spellId(): Int {
        return 74
    }

    override fun exp(): Double {
        return 120.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }

    override fun cast(player: Player, target: Actor) {
        val lastTimeCast = player.getTemporaryAttribute("LAST_VENG", -1L)
        if (player.skills.getLevel(SkillConstants.MAGIC) < 94) {
            player.packets.sendMessage("Your Magic level is not high enough for this spell.")
            return
        } else if (player.skills.getLevel(SkillConstants.DEFENCE) < 40) {
            player.packets.sendMessage("You need a Defence level of 40 for this spell")
            return
        } else if (lastTimeCast != null && lastTimeCast + 30000 > System.currentTimeMillis()) {
            player.packets.sendMessage(
                "You must wait " + TimeUnit.MILLISECONDS.toSeconds(
                    lastTimeCast + 30000 - System.currentTimeMillis()
                ) + " more seconds to cast vengeance."
            )
            return
        } else if (!CombatAlgorithm.checkRunes(
                player,
                true,
                MagicConstants.ASTRAL_RUNE,
                4,
                MagicConstants.DEATH_RUNE,
                3,
                MagicConstants.EARTH_RUNE,
                11
            )
        ) {
            return
        }
        // the amount of people the spell affected
        var affectedPeopleCount = 0
        for (regionId in player.mapRegionsIds) {
            val region = RegionManager.getRegion(regionId)
            val playerIndexes = region.playerIndexes ?: continue
            for (playerIndex in playerIndexes) {
                val other = World.getPlayers()[playerIndex]
                // skip the unavailable players first
                if (other == null || other === player || other.isDead || !other.withinDistance(player, 4)) {
                    continue
                }
                /*
				// lets the caster know the other person needs to have accept aid on
				if (!other.getVariables().isAcceptingAid()) {
					player.getPackets().sendGameMessage(other.getDetails().getDisplayName() + " is not accepting aid.");
					continue;
				}
				 */
                // visual
                other.setNextGraphics(GRAPHICS)
                // attributes
                other.putTemporaryAttribute("cast_veng", true)
                other.putTemporaryAttribute("LAST_VENG", System.currentTimeMillis())
                // increment total amount
                affectedPeopleCount++
            }
        }

        // visuals
        player.nextAnimation = ANIMATION
        player.skills.addXp(SkillConstants.MAGIC, exp())
        player.packets.sendMessage("The spell affected $affectedPeopleCount nearby people.")

        // store attribute information
        player.putTemporaryAttribute("LAST_VENG", System.currentTimeMillis())
    }

    companion object {
        private val ANIMATION = Animation(4411)
        private val GRAPHICS = Graphics(725, 0, 100)
    }
}
package plugin.interaction.combat.magic.lunar

import game.content.entity.actor.combat.CombatAlgorithm
import game.content.plugin.combat.spell.type.RegularSpellPlugin
import game.entity.actor.Actor
import game.entity.actor.mask.Animation
import game.entity.actor.mask.Graphics
import game.entity.actor.player.Player
import utility.constants.MagicConstants
import utility.constants.MagicConstants.MagicBook
import utility.constants.SkillConstants
import java.util.concurrent.TimeUnit

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/8/2017
 */
class VengeanceSpellPlugin : RegularSpellPlugin {
    override fun spellId(): Int {
        return 37
    }

    override fun exp(): Double {
        return 112.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }

    override fun cast(player: Player, target: Actor) {
        val lastTimeCast = player.getTemporaryAttribute("last_veng_time", -1L)
        if (player.skills.getLevel(SkillConstants.MAGIC) < 94) {
            player.packets.sendMessage("Your Magic level is not high enough for this spell.")
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
                2,
                MagicConstants.EARTH_RUNE,
                10
            )
        ) {
            return
        }
        player.setNextGraphics(Graphics(726, 0, 100))
        player.nextAnimation = Animation(4410)
        player.putTemporaryAttribute("cast_veng", true)
        player.putTemporaryAttribute("last_veng_time", System.currentTimeMillis())
    }
}
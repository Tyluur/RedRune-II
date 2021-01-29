package plugin.command.administrator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.SkillConstants
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/5/2017
 */
@CommandManifest(description = "Heals you to maximum health")
class HealCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        player.combatDefinitions.setSpecialAttack(100)
        player.poisonManager.reset()
        player.prayer.prayerpoints = (player.skills.getLevelForXp(SkillConstants.PRAYER) * 10 * 1.15).toInt()
        player.prayer.refreshPrayerPoints()
        player.heal(player.maxHitpoints, (player.skills.getLevelForXp(SkillConstants.HITPOINTS) * 10 * 0.05).toInt())
        player.skills.restoreSkills()
        player.attributes.setRunEnergy(100)
    }

    override fun identifiers(): Array<String> {
        return arguments("heal")
    }
}
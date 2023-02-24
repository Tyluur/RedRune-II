package plugin.command.administrator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.SkillConstants
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Sets your stats to max")
class MasterCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        for (skill in 0..24) {
            player.skills[skill] = 99
            player.skills.setXp(skill, SkillConstants.getXPForLevel(99).toDouble())
        }
        player.skills.restoreSkills()
    }

    override fun identifiers(): Array<String> {
        return arguments("master")
    }
}
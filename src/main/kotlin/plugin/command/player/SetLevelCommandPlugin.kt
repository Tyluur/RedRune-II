package plugin.command.player

import plugin.command.CommandManifest
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.global.World
import org.redrune.utility.constants.SkillConstants
import plugin.command.player.YellCommandPlugin
import org.redrune.utility.functions.Misc
import org.redrune.game.entity.actor.player.data.PlayerRight
import org.redrune.utility.constants.InterfaceConstants
import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Sets your levels", types = [Int::class, Int::class])
class SetLevelCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val skill = intParam(args, 1)
        val level = intParam(args, 2)
        if (level < 0 || level > 99) {
            player.packets.sendMessage("Please choose a valid level.")
            return
        }
        player.skills[skill] = level
        player.skills.setXp(skill, SkillConstants.getXPForLevel(level).toDouble())
        player.appearance.generateAppearanceData()
    }

    override fun identifiers(): Array<String> {
        return arguments("setlevel")
    }
}
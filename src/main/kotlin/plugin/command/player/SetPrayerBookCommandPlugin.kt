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
 * @since 9/8/2017
 */
@CommandManifest(description = "Sets your prayer book [1/2]", types = [Int::class])
class SetPrayerBookCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val bookId = intParam(args, 1)
        player.prayer.setPrayerBook(bookId == 2)
    }

    override fun identifiers(): Array<String> {
        return arguments("setprayerbook")
    }
}
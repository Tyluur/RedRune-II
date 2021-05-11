package plugin.command.player

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import plugin.command.CommandManifest

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
package plugin.command.player

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/8/2017
 */
@CommandManifest(description = "Sets your magic book [1-3]", types = [Int::class])
class SetMagicBookCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val book = intParam(args, 1)
        if (book < 1 || book > 3) {
            player.packets.sendMessage("You can only enter a book id between [1-3]")
            return
        }
        val bookId = book - 1
        player.combatDefinitions.spellBook = bookId
    }

    override fun identifiers(): Array<String> {
        return arguments("setmagicbook")
    }
}
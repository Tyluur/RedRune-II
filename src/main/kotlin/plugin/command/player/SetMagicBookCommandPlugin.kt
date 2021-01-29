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
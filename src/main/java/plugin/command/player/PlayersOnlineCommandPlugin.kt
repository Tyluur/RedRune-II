package plugin.command.player

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import org.redrune.utility.constants.InterfaceConstants
import plugin.command.CommandManifest
import java.util.*

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Shows the players online")
class PlayersOnlineCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val messages: MutableList<String> = ArrayList()
        World.playerStream()
            .forEach { p: Player -> messages.add("" + p.displayName + " (lvl. " + p.skills.combatLevel + ")") }
        sendResponse(player, "There are currently " + World.getPlayers().size + " players online.", console)

        InterfaceConstants.sendQuestScroll(player, "Dusk", *messages.toTypedArray())
    }

    override fun identifiers(): Array<String> {
        return arguments("players")
    }
}
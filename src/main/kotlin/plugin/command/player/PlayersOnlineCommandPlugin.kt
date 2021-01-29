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
import java.util.ArrayList

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
        player.packets.sendMessage("There are currently " + World.getPlayers().size + " players online.", console)
        InterfaceConstants.sendQuestScroll(player, "Dusk", *messages.toTypedArray())
    }

    override fun identifiers(): Array<String> {
        return arguments("players")
    }
}
package plugin.command.owner

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/12/2017
 */
class DebugCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val componentId = intParam(args, 1)
        player.packets.sendIComponentText(1149, componentId, "" + componentId)
    }

    override fun identifiers(): Array<String> {
        return arguments("dbg")
    }
}
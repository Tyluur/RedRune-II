package plugin.command.owner

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-01-29
 */
class SendVarCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val id = intParamOrDefault(args, 1, 0)
        val value = intParamOrDefault(args, 2, 0)
        player.varManager.sendVar(id, value)
    }

    override fun identifiers(): Array<String> {
        return arguments("sendvar")
    }
}
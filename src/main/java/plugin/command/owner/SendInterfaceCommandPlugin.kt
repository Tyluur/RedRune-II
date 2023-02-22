package plugin.command.owner

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Displays an interface by its id", types = [Int::class])
class SendInterfaceCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val interfaceId = intParam(args, 1)
        player.interfaceManager.sendInterface(interfaceId)
    }

    override fun identifiers(): Array<String> {
        return arguments("sendinterface", "inter", "interface")
    }
}
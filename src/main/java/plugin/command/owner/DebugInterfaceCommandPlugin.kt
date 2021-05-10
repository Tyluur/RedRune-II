package plugin.command.owner

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.functions.Misc
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-02-01
 */
@CommandManifest(description = "Sends all the components of an interface", types = [Int::class])
class DebugInterfaceCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val interfaceId = intParam(args, 1)
        val componentLength = Misc.getInterfaceDefinitionsComponentsSize(interfaceId)
        var sendInterface = true
        if (args.size == 3) {
            sendInterface = java.lang.Boolean.parseBoolean(args[2])
        }
        for (i in 0 until componentLength) {
            player.packets.sendIComponentText(interfaceId, i, "" + i)
        }
        for (i in 0..354) {
            player.packets.sendGlobalString(i, "g$i")
        }
        if (sendInterface) player.interfaceManager.sendInterface(interfaceId)
        println("Component length: $componentLength")
    }

    override fun identifiers(): Array<String> {
        return arguments("dbi")
    }
}
package plugin.command.administrator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.functions.Misc
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Shows how much memory is used.")
class MemoryUsageCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val info = Misc.getMemoryUsageInformation()
        println(info)
        player.packets.sendMessage(info)
    }

    override fun identifiers(): Array<String> {
        return arguments("memused")
    }
}
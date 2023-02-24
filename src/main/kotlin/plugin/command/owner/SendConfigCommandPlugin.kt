package plugin.command.owner

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-24
 */
@CommandManifest(types = [Int::class, Int::class])
class SendConfigCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val configId = intParam(args, 1)
        val configValue = intParam(args, 2)
        player.packets.sendConfig(configId, configValue)
    }

    override fun identifiers(): Array<String> {
        return arguments("sendconfig")
    }
}
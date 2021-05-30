package plugin.command.administrator

import org.redrune.engine.SystemManager
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Updates the server after x seconds", types = [Int::class])
class UpdateCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val delay = intParam(args, 1)
        SystemManager.safeShutdown(delay)
    }

    override fun identifiers(): Array<String> {
        return arguments("update", "shutdown")
    }
}
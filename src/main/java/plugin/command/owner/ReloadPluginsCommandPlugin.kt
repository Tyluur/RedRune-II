package plugin.command.owner

import game.content.plugin.PluginRepository
import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Reloads all plugins")
class ReloadPluginsCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        PluginRepository.reload()
        sendResponse(player, "All plugins have been reloaded.", clientCommand)
    }

    override fun identifiers(): Array<String> {
        return arguments("reloadplugins", "rlp")
    }
}
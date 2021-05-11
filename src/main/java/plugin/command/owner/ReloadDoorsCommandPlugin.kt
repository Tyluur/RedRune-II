package plugin.command.owner

import game.content.plugin.type.CommandPlugin
import plugin.command.CommandManifest
import game.entity.actor.player.Player
import utility.game.repository.`object`.door.DoorRepository

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-02-20
 */
@CommandManifest(description = "Reloads all doors from file")
class ReloadDoorsCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        DoorRepository.initialize()
        player.packets.sendMessage("Reloaded all game doors")
    }

    override fun identifiers(): Array<String> {
        return arguments("rldrs", "rldoors")
    }
}
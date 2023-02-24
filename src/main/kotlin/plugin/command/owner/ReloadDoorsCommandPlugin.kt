package plugin.command.owner

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.game.repository.door.DoorRepository
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur@icloud.com>
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
package plugin.command.owner

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-02-20
 */
@CommandManifest(description = "Toggles door finding ")
class FindBestDoorPossibilitiesCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        player.putTemporaryAttribute("door_finding", !player.getTemporaryAttribute("door_finding", false))
        player.packets.sendMessage(
            "You are now " + (if (player.getTemporaryAttribute(
                    "door_finding",
                    false
                )
            ) "finding" else "not finding") + " possible doors."
        )
    }

    override fun identifiers(): Array<String> {
        return arguments("toggledoorfind")
    }
}
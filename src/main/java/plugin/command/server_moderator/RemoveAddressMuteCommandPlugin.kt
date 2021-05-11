package plugin.command.server_moderator

import plugin.command.CommandManifest
import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import game.global.punishment.PunishmentHandler
import game.global.punishment.PunishmentType

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/13/2017
 */
@CommandManifest(description = "Removes an address mute for a player", types = [String::class])
class RemoveAddressMuteCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val name = getCompleted(args, 1)
        PunishmentHandler.removePunishment(player, name, PunishmentType.ADDRESS_MUTE)
    }

    override fun identifiers(): Array<String> {
        return arguments("unipmute")
    }
}
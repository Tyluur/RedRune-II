package plugin.command.server_moderator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.punishment.PunishmentHandler
import org.redrune.game.global.punishment.PunishmentType
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur@icloud.com>
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
package plugin.command.server_moderator

import plugin.command.CommandManifest
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.game.InputEvent.InputEventType
import org.redrune.game.global.punishment.PunishmentHandler
import org.redrune.game.global.punishment.PunishmentType

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/13/2017
 */
@CommandManifest(description = "Unmutes a player", types = [String::class])
class UnmuteCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val name = getCompleted(args, 1)
        PunishmentHandler.removePunishment(player, name, PunishmentType.PLAYER_MUTE)
    }

    override fun identifiers(): Array<String> {
        return arguments("unmute")
    }
}
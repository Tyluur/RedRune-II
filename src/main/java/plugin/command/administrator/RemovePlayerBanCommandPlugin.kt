package plugin.command.administrator

import plugin.command.CommandManifest
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.global.punishment.PunishmentHandler
import org.redrune.game.global.punishment.PunishmentType
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/13/2017
 */
@CommandManifest(description = "Unbans a player by their username", types = [String::class])
class RemovePlayerBanCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val name = getCompleted(args, 1)
        PunishmentHandler.removePunishment(player, name, PunishmentType.PLAYER_BAN)
    }

    override fun identifiers(): Array<String> {
        return arguments("unban")
    }
}
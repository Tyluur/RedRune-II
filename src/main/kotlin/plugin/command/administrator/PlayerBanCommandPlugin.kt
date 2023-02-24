package plugin.command.administrator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.punishment.PunishmentHandler
import org.redrune.game.global.punishment.PunishmentType
import org.redrune.utility.game.InputEvent
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/13/2017
 */
@CommandManifest(description = "Bans a player", types = [String::class])
class PlayerBanCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val name = getCompleted(args, 1)
        player.packets.requestClientInput(object :
            InputEvent("Enter Duration (HRS (0 = inf)):", InputEventType.INTEGER) {
            override fun handleInput() {
                PunishmentHandler.addPunishment(player, name, getInput(), PunishmentType.PLAYER_BAN)
            }
        })
    }

    override fun identifiers(): Array<String> {
        return arguments("ban")
    }
}
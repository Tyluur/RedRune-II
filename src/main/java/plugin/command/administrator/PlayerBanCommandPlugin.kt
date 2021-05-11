package plugin.command.administrator

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import game.global.punishment.PunishmentHandler
import game.global.punishment.PunishmentType
import utility.game.InputEvent
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
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
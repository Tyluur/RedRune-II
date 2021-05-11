package plugin.command.player

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import game.entity.actor.player.data.PlayerRight
import game.global.World
import utility.constants.ColorConstants
import utility.functions.Misc
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Yells a message to everyone online", types = [String::class])
class YellCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val message = getCompleted(args, 1)
        if (message == null || message.equals("null", ignoreCase = true)) {
            return
        }
        if (!player.rightsContains(
                PlayerRight.DONATOR,
                PlayerRight.SERVER_MODERATOR,
                PlayerRight.ADMINISTRATOR,
                PlayerRight.OWNER
            )
        ) {
            player.packets.sendMessage("You do not have access to the yell channel!")
            return
        }
        sendYellMessage(player, message)
    }

    override fun identifiers(): Array<String> {
        return arguments("yell")
    }

    companion object {
        /**
         * Sends a yell message
         *
         * @param player
         * The player yelling
         * @param message
         * The message
         */
        fun sendYellMessage(player: Player, message: String) {
            var message = message
            message = Misc.fixChatMessage(message.replace("<".toRegex(), "")).trim { it <= ' ' }
            val tag = StringBuilder()
            tag.append("[<col=" + ColorConstants.BLUE + ">Dusk</col>] ")
            tag.append(player.displayName).append(": ").append(message)
            for (pl in World.getPlayers()) {
                if (pl == null) {
                    continue
                }
                pl.packets.sendMessage(tag.toString())
            }
        }
    }
}
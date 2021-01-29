package plugin.command.player

import plugin.command.CommandManifest
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.global.World
import org.redrune.utility.constants.SkillConstants
import plugin.command.player.YellCommandPlugin
import org.redrune.utility.functions.Misc
import org.redrune.game.entity.actor.player.data.PlayerRight
import org.redrune.utility.constants.InterfaceConstants
import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.ColorConstants
import java.lang.StringBuilder

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
            tag.append("[<col=" + ColorConstants.BLUE + ">RR</col>] ")
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
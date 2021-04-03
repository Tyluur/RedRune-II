package plugin.command.owner

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/12/2017
 */
class DebugCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        logger.info { "inventory=${player.inventory.items}" }
        logger.info { "equipment=${player.equipment.items}" }
        logger.info { "skills=${player.skills}" }
    }

    override fun identifiers(): Array<String> {
        return arguments("dbg")
    }

    companion object {
        private val logger = InlineLogger()
    }
}
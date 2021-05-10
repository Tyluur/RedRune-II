package plugin.command.administrator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Transforms you into an npc [-1 for human]", types = [Int::class])
class PNPCCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val npcId = intParam(args, 1)
        player.appearance.transformIntoNPC(npcId)
    }

    override fun identifiers(): Array<String> {
        return arguments("pnpc")
    }
}
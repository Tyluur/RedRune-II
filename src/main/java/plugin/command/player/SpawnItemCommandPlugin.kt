package plugin.command.player

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Spawns an item", types = [Int::class])
class SpawnItemCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val itemId = intParam(args, 1)
        val amount = intParamOrDefault(args, 2, 1)
        player.inventory.addItem(itemId, amount)
    }

    override fun identifiers(): Array<String> {
        return arguments("item", "pickup")
    }
}
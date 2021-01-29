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

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/8/2017
 */
@CommandManifest(description = "Spawns all the runes you'll ever need")
class SpawnRunesCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val amount = 10000
        for (i in 554..566) {
            player.inventory.addItem(i, amount)
        }
        player.inventory.addItem(9075, amount)
    }

    override fun identifiers(): Array<String> {
        return arguments("runes")
    }
}
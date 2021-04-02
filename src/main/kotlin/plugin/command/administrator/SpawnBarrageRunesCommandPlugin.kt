package plugin.command.administrator

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since January 28, 2021
 */
class SpawnBarrageRunesCommandPlugin : CommandPlugin() {

    override fun handle(player: Player, args: Array<out String>, console: Boolean, clientCommand: Boolean) {
        val inventory = player.inventory

        inventory.addItem(565, 5000)
        inventory.addItem(560, 5000)
        inventory.addItem(555, 5000)
    }

    override fun identifiers() = arrayOf("barrage")

}
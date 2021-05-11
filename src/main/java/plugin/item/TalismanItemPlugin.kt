package plugin.item

import game.content.entity.actor.player.skills.runecrafting.Runecrafting
import game.content.plugin.type.ItemPlugin
import game.entity.actor.player.Player
import game.entity.item.Item

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class TalismanItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        val itemId = item.id
        if (itemId == 1438) {
            Runecrafting.locate(player, 3127, 3405)
        } else if (itemId == 1440) {
            Runecrafting.locate(player, 3306, 3474)
        } else if (itemId == 1442) {
            Runecrafting.locate(player, 3313, 3255)
        } else if (itemId == 1444) {
            Runecrafting.locate(player, 3185, 3165)
        } else if (itemId == 1446) {
            Runecrafting.locate(player, 3053, 3445)
        } else if (itemId == 1448) {
            Runecrafting.locate(player, 2982, 3514)
        }
        return true
    }

    override fun register() {
        val ids = intArrayOf(1438, 1440, 1442, 1444, 1446, 1448)
        for (id in ids) {
            registerItem(id, "Locate")
        }
    }
}
package plugin.item

import org.redrune.game.content.entity.actor.player.skills.runecrafting.Runecrafting
import org.redrune.game.content.plugin.type.ItemPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class RunecraftingPouchItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        val itemId = item.id
        var pouch = -1
        if (itemId == 5509) {
            pouch = 0
        } else if (itemId == 5510) {
            pouch = 1
        } else if (itemId == 5512) {
            pouch = 2
        } else if (itemId == 5514) {
            pouch = 3
        }
        if (pouch == -1) {
            return false
        }
        when (option) {
            "Fill" -> Runecrafting.fillPouch(player, pouch)
            "Empty" -> Runecrafting.emptyPouch(player, pouch)
            "Check" -> Runecrafting.checkPouch(player, pouch)
        }
        return true
    }

    override fun register() {
        for (itemId in 5509..5514) {
            registerItem(itemId, "Fill")
            registerItem(itemId, "Empty")
            registerItem(itemId, "Check")
        }
    }
}
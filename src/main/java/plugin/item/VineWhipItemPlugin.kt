package plugin.item

import game.content.plugin.type.ItemOnItemPlugin
import game.content.plugin.type.ItemPlugin
import game.entity.actor.player.Player
import game.entity.item.Item

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class VineWhipItemPlugin : ItemPlugin, ItemOnItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        if (!player.attributes.isCanPvp) {
            if (player.inventory.freeSlots > 1) {
                player.inventory.deleteItem(21371, 1)
                player.inventory.addItem(4151, 1)
                player.inventory.addItem(21369, 1)
                player.packets.sendMessage("You split the vine and whip apart.")
            } else {
                player.packets.sendMessage("You need two inventory spaces to do this.")
            }
        } else {
            player.packets.sendMessage("You can not do this in the wilderness.")
        }
        return true
    }

    override fun handleItemOnItem(player: Player, used: Item, with: Item): Boolean {
        if (!player.inventory.containsItems(intArrayOf(4151, 21369), intArrayOf(1, 1))) {
            return false
        }
        player.inventory.deleteItem(21369, 1)
        player.inventory.deleteItem(4151, 1)
        player.inventory.addItem(21371, 1)
        player.packets.sendMessage("You have successfully combined a vine and a whip")
        return true
    }

    override fun register() {
        registerItem(21371, "Split")
        registerItemOnItemIds(21369, 4151)
    }
}
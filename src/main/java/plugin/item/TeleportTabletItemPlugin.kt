package plugin.item

import game.content.entity.actor.combat.function.Magic
import game.content.plugin.type.ItemPlugin
import game.entity.actor.player.Player
import game.entity.item.Item

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-07
 */
class TeleportTabletItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        Magic.useTabTeleport(player, item.id)
        return true
    }

    override fun register() {
        for (itemId in 8007..8013) {
            registerItem(itemId, "Break")
        }
    }
}
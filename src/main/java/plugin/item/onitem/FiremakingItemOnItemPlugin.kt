package plugin.item.onitem

import game.content.entity.actor.player.skills.firemaking.Firemaking
import game.content.plugin.type.ItemOnItemPlugin
import game.entity.actor.player.Player
import game.entity.item.Item
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class FiremakingItemOnItemPlugin : ItemOnItemPlugin {
    override fun handleItemOnItem(player: Player, used: Item, with: Item): Boolean {
        Firemaking.isFiremaking(player, used, with)
        return true
    }

    override fun register() {
        Arrays.stream(Firemaking.Fire.values())
            .forEach { fire: Firemaking.Fire -> registerItemOnItemIds(fire.logId, 590) }
    }
}
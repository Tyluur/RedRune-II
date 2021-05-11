package plugin.item

import game.content.entity.item.Burying
import game.content.entity.item.Burying.Bone
import game.content.plugin.type.ItemPlugin
import game.entity.actor.player.Player
import game.entity.item.Item
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-07
 */
class BoneBurialItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        Burying.bury(player, slotId)
        return true
    }

    override fun register() {
        Arrays.stream(Bone.values()).forEach { bone: Bone -> registerItem(bone.id, "Bury") }
    }
}
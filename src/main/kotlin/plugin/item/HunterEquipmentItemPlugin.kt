package plugin.item

import org.redrune.game.content.entity.actor.player.skills.hunter.Hunter
import org.redrune.game.content.plugin.type.ItemPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class HunterEquipmentItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        val itemId = item.id
        when (itemId) {
            10006 -> player.actionManager.action = Hunter(Hunter.HunterEquipment.BRID_SNARE)
            10008 -> player.actionManager.action = Hunter(Hunter.HunterEquipment.BOX)
        }
        return true
    }

    override fun register() {
        registerItem(Hunter.HunterEquipment.BOX.id, "Lay")
        registerItem(Hunter.HunterEquipment.BRID_SNARE.id, "Lay")
    }
}
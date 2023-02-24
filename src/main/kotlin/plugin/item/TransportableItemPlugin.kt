package plugin.item

import org.redrune.game.content.plugin.type.ItemPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import org.redrune.game.global.WorldTile

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class TransportableItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        val itemId = item.id
        if (itemId in 1706..1712 || itemId in 10354..10360) {
            player.dialogueManager.startDialogue(
                "Transportation",
                "Edgeville",
                WorldTile(3087, 3496, 0),
                "Karamja",
                WorldTile(2918, 3176, 0),
                "Draynor Village",
                WorldTile(3105, 3251, 0),
                "Al Kharid",
                WorldTile(3293, 3163, 0),
                itemId
            )
        } else if (itemId == 1704 || itemId == 10362) {
            player.packets.sendMessage("The amulet has ran out of charges. You need to recharge it if you wish it use it once more.")
        } else if (itemId in 3853..3867) {
            player.dialogueManager.startDialogue(
                "Transportation",
                "Burthrope Games Room",
                WorldTile(2880, 3559, 0),
                "Barbarian Outpost",
                WorldTile(2519, 3571, 0),
                "Gamers' Grotto",
                WorldTile(2970, 9679, 0),
                "Corporeal Beast",
                WorldTile(2886, 4377, 0),
                itemId
            )
        }
        return true
    }

    override fun register() {
        // reg glory
        registerItem(1704, "Rub")
        registerItem(1706, "Rub")
        registerItem(1708, "Rub")
        registerItem(1710, "Rub")
        registerItem(1712, "Rub")
        // glory (trimmed)
        registerItem(10354, "Rub")
        registerItem(10356, "Rub")
        registerItem(10358, "Rub")
        registerItem(10360, "Rub")
        registerItem(10362, "Rub")
        // games necklace
        registerItem(3853, "Rub")
        registerItem(3855, "Rub")
        registerItem(3857, "Rub")
        registerItem(3859, "Rub")
        registerItem(3861, "Rub")
        registerItem(3863, "Rub")
        registerItem(3865, "Rub")
        registerItem(3867, "Rub")
    }
}
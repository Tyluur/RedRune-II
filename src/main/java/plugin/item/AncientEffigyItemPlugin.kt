package plugin.item

import game.content.plugin.type.ItemPlugin
import game.entity.actor.player.Player
import game.entity.item.Item

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class AncientEffigyItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        player.dialogueManager.startDialogue("AncientEffigiesD", item.id)
        return true
    }

    override fun register() {
        for (i in 18788 downTo 18781) {
            registerItem(i, "Investigate")
        }
    }
}
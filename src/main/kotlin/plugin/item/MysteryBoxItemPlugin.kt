package plugin.item

import org.redrune.game.content.plugin.type.ItemPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.actor.player.data.PlayerRight
import org.redrune.game.entity.item.Item

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since March 19, 2021
 */
class MysteryBoxItemPlugin : ItemPlugin {
    override fun register() {
        registerItem(6199, "Open")
    }

    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        player.giveRight(PlayerRight.PREMIUM_DONATOR)
        player.inventory.deleteItem(item)
        player.dialogueManager.startDialogue("SimpleMessage", "Welcome to the Dusk Donator life boi.")
        return true
    }
}
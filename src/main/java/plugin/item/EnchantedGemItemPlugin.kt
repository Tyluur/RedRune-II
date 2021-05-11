package plugin.item

import game.content.plugin.type.ItemPlugin
import game.entity.actor.player.Player
import game.entity.item.Item

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class EnchantedGemItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        player.dialogueManager.startDialogue("EnchantedGemDialouge")
        return true
    }

    override fun register() {
        registerItem(4155, "Activate")
        //		registerItem(4155, "Kills-left");
    }
}
package plugin.item

import game.content.entity.actor.player.skills.SkillCapeCustomizer
import game.content.plugin.type.ItemPlugin
import game.entity.actor.player.Player
import game.entity.item.Item

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class SkillcapeItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        when (option) {
            "Customise" -> SkillCapeCustomizer.startCustomizing(player, item.id)
            "Features" -> player.dialogueManager.startDialogue("CompCape", item.id)
        }
        return true
    }

    override fun register() {
        registerItem(20767, "Customise")
        registerItem(20769, "Customise")
        registerItem(20771, "Customise")
        registerItem(20769, "Features")
        registerItem(20771, "Features")
    }
}
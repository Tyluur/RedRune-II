package plugin.`object`

import game.content.plugin.type.ObjectPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since March 19, 2021
 */
class HomeAltarObjectPlugin : ObjectPlugin {
    override fun register() {
        registerObject(47120, "Pray")
    }

    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        player.dialogueManager.startDialogue("ZarosAltar")
        return true
    }
}
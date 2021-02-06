package plugin.`object`

import org.redrune.game.content.plugin.type.ObjectPlugin
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
class NexEntranceObjectPlugin : ObjectPlugin {
    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        when (option) {
            "Climb-over" -> {
                player.dialogueManager.startDialogue("NexEntrance")
                return true
            }
        }
        return false
    }

    override fun register() {
        registerObject(57225, "Climb-over")
    }
}
package plugin.`object`

import game.content.entity.`object`.ClimbActionHandler
import game.content.plugin.type.ObjectPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.player.Player
import utility.game.repository.`object`.climbable.ClimbableObjectRepository

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-21
 */
class ClimbableObjectPlugin : ObjectPlugin {
    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        ClimbActionHandler.climbLadder(player, `object`, option.toLowerCase())
        return true
    }

    override fun register() {
        for (objectId in ClimbableObjectRepository.objectIds) {
            registerObject(objectId, "Climb")
            registerObject(objectId, "Climb-up")
            registerObject(objectId, "Climb-down")
        }
    }
}
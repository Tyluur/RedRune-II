package plugin.`object`

import engine.tick.task.WorldTask
import engine.tick.task.WorldTasksManager
import game.content.plugin.type.ObjectPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.mask.Animation
import game.entity.actor.mask.ForceMovement
import game.entity.actor.player.Player
import game.global.WorldTile
import utility.game.ClickOption

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-31
 */
class WildernessDitchObjectPlugin : ObjectPlugin {
    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        performJump(player, `object`, null)
        return true
    }

    override fun register() {
        for (i in 1440..1444) {
            registerSpecifiedOption(ClickOption.FIRST, i)
        }
    }

    companion object {
        @JvmStatic
        fun performJump(player: Player, `object`: WorldObject, onJump: Runnable?) {
            player.locks.lock()
            player.nextAnimation = Animation(6132)
            val `in`: Boolean = player.y < `object`.y
            val toTile =
                WorldTile(player.x, if (`in`) `object`.y + 2 else `object`.y - 1, `object`.plane)
            player.nextForceMovement =
                ForceMovement(WorldTile(player), 1, toTile, 2, if (`in`) ForceMovement.NORTH else ForceMovement.SOUTH)
            WorldTasksManager.schedule(object : WorldTask() {
                override fun run() {
                    val faceTile = WorldTile(player.x, player.y + if (`in`) 1 else -1, player.plane)
                    player.nextFaceWorldTile = faceTile
                    player.setNextWorldTile(toTile)
                    player.locks.unlock()
                    onJump?.run()
                }
            }, 2)
        }
    }
}
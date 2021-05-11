package plugin.interaction.city

import engine.tick.task.WorldTask
import engine.tick.task.WorldTasksManager
import game.content.entity.actor.player.skills.agility.Agility
import game.content.plugin.type.ObjectPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.mask.Animation
import game.entity.actor.mask.ForceMovement
import game.entity.actor.player.Player
import game.global.WorldTile
import utility.constants.SkillConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-31
 */
class FaladorCityPlugin : ObjectPlugin {
    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        val id: Int = `object`.id
        when (id) {
            11844 -> {
                if (!Agility.hasLevel(player, 5)) {
                    return true
                }
                player.locks.lock()
                val toTile = WorldTile(if (player.x >= 2936) 2934 else 2936, 3355, 0)
                player.nextForceMovement = ForceMovement(
                    player.worldTile,
                    1,
                    toTile,
                    2,
                    if (player.x == 2934) ForceMovement.EAST else ForceMovement.WEST
                )
                player.nextAnimation = Animation(839)
                WorldTasksManager.schedule(object : WorldTask() {
                    var stage = 0
                    override fun run() {
                        if (stage == 1) {
                            player.setNextWorldTile(toTile)
                            player.skills.addXp(SkillConstants.AGILITY, 0.5)
                            player.nextAnimation = Animation(-1)
                            player.locks.unlock()
                            stop()
                        }
                        stage++
                    }
                }, 0, 1)
            }
        }
        return true
    }

    override fun register() {
        registerObject(11844, "Climb-over")
    }
}
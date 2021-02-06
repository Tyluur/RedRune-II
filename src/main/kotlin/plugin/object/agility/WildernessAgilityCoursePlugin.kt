package plugin.`object`.agility

import org.redrune.game.content.entity.actor.player.skills.agility.WildernessAgility
import org.redrune.game.content.plugin.type.ObjectPlugin
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.game.ClickOption

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-29
 */
class WildernessAgilityCoursePlugin : ObjectPlugin {
    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        val id: Int = `object`.getId()
        when (id) {
            2297 -> WildernessAgility.walkAcrossLogBalance(player, `object`)
            37704 -> WildernessAgility.jumpSteppingStones(player, `object`)
            2288 -> WildernessAgility.enterWildernessPipe(player, `object`.getX(), `object`.getY())
            2328 -> WildernessAgility.climbUpWall(player, `object`)
            2283 -> WildernessAgility.swingOnRopeSwing(player, `object`)
            2309 -> WildernessAgility.enterWildernessCourse(player)
            2307, 2308 -> WildernessAgility.exitWildernessCourse(player)
        }
        return true
    }

    override fun register() {
        registerSpecifiedOptionVarags(ClickOption.FIRST, 2297, 37704, 2288, 2328, 2283, 2309, 2307, 2308)
    }
}
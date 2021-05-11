package plugin.`object`.agility

import game.content.entity.actor.player.skills.agility.BarbarianOutpostAgility
import game.content.plugin.type.ObjectPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.player.Player
import utility.game.ClickOption

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-29
 */
class BarbarianAgilityCoursePlugin : ObjectPlugin {
    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        val id: Int = `object`.id
        val x: Int = `object`.x
        val y: Int = `object`.y
        if (id == 20210) {
            BarbarianOutpostAgility.enterObstaclePipe(player, `object`)
        } else if (id == 43526) {
            BarbarianOutpostAgility.swingOnRopeSwing(player, `object`)
        } else if (id == 43595 && x == 2550 && y == 3546) {
            BarbarianOutpostAgility.walkAcrossLogBalance(player, `object`)
        } else if (id == 20211 && x == 2538 && y == 3545) {
            BarbarianOutpostAgility.climbObstacleNet(player, `object`)
        } else if (id == 2302 && x == 2535 && y == 3547) {
            BarbarianOutpostAgility.walkAcrossBalancingLedge(player, `object`)
        } else if (id == 1948) {
            BarbarianOutpostAgility.climbOverCrumblingWall(player, `object`)
        } else if (id == 43533) {
            BarbarianOutpostAgility.runUpWall(player, `object`)
        } else if (id == 43597) {
            BarbarianOutpostAgility.climbUpWall(player, `object`)
        } else if (id == 43587) {
            BarbarianOutpostAgility.fireSpringDevice(player, `object`)
        } else if (id == 43527) {
            BarbarianOutpostAgility.crossBalanceBeam(player, `object`)
        } else if (id == 43531) {
            BarbarianOutpostAgility.jumpOverGap(player, `object`)
        } else if (id == 43532) {
            BarbarianOutpostAgility.slideDownRoof(player, `object`)
        }
        return true
    }

    override fun register() {
        registerSpecifiedOptionVarags(
            ClickOption.FIRST,
            20210,
            43526,
            43595,
            20211,
            2302,
            1948,
            43533,
            43597,
            43587,
            43527,
            43531,
            43532
        )
    }
}
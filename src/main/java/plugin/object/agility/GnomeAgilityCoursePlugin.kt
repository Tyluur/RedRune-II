package plugin.`object`.agility

import org.redrune.game.content.entity.actor.player.skills.agility.GnomeAgility
import org.redrune.game.content.plugin.type.ObjectPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.utility.game.ClickOption

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-29
 */
class GnomeAgilityCoursePlugin : ObjectPlugin {
    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        val id: Int = `object`.id
        if (id == 2295) {
            GnomeAgility.walkGnomeLog(player)
        } else if (id == 2285) {
            GnomeAgility.climbGnomeObstacleNet(player)
        } else if (id == 35970) {
            GnomeAgility.climbUpGnomeTreeBranch(player)
        } else if (id == 2312) {
            GnomeAgility.walkGnomeRope(player)
        } else if (id == 4059) {
            GnomeAgility.walkBackGnomeRope(player)
        } else if (id == 2314) {
            GnomeAgility.climbDownGnomeTreeBranch(player)
        } else if (id == 2286) {
            GnomeAgility.climbGnomeObstacleNet2(player)
        } else if (id == 43544 || id == 43543) {
            GnomeAgility.enterGnomePipe(player, `object`.x, `object`.y)
        } else if (id == 43528) {
            GnomeAgility.climbUpTree(player)
        } else if (id == 43529) {
            GnomeAgility.preSwing(player, `object`)
        } else if (id == 43539) {
            GnomeAgility.jumpDown(player, `object`)
        }
        return true
    }

    override fun register() {
        registerSpecifiedOptionVarags(
            ClickOption.FIRST,
            2295,
            2285,
            35970,
            2312,
            4059,
            2314,
            2286,
            43544,
            43543,
            43528,
            43529,
            43539
        )
    }
}
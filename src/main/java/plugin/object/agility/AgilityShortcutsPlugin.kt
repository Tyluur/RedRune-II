package plugin.`object`.agility

import game.content.entity.actor.player.skills.agility.Shortcuts
import game.content.plugin.type.ObjectPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.player.Player
import utility.game.ClickOption

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-29
 */
class AgilityShortcutsPlugin : ObjectPlugin {
    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        val id: Int = `object`.id
        if (id == 9311 || id == 9312) {
            Shortcuts.handleEdgevilleUnderwallTunnel(player, `object`)
        }
        return true
    }

    override fun register() {
        registerSpecifiedOptionVarags(ClickOption.FIRST, 9311, 9312)
    }
}
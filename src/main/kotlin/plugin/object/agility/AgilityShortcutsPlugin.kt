package plugin.`object`.agility

import org.redrune.game.content.entity.actor.player.skills.agility.Shortcuts
import org.redrune.game.content.plugin.type.ObjectPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.utility.game.ClickOption

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
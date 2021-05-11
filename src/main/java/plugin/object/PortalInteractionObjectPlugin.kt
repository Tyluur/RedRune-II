package plugin.`object`

import game.content.plugin.type.ObjectPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.player.Player
import game.global.WorldTile

class PortalInteractionObjectPlugin : ObjectPlugin {
    override fun register() {
        registerObject(42220, "Leave-area")
        registerObject(42219, "Enter")
    }

    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        when (`object`.id) {
            // leave sw
            42220 -> {
                player.setNextWorldTile(WorldTile(3082, 3475, 0))
            }
            // enter sw
            42219 -> {
                player.setNextWorldTile(WorldTile(1886, 3178, 0))
            }
        }
        return true
    }
}
package plugin.minigame

import org.redrune.game.content.plugin.type.ObjectPlugin
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.entity.actor.player.Player

class SoulWarsObjectPlugin : ObjectPlugin {
    override fun register() {
        registerObject(42031, "Join-team")
    }

    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        when (`object`.id) {
            // guthix portal
            42031 -> {

            }
            // saradomin portal
            42029 -> {

            }
            // zamorak portal
            42030 -> {

            }
        }
        return true
    }
}
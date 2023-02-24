package plugin.`object`.minigame

import org.redrune.game.content.plugin.type.ObjectPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.`object`.WorldObject
import plugin.rsinterface.TeleportationInterfacePlugin

class SoulWarsObjectPlugin : ObjectPlugin {
    override fun register() {
        registerObject(42031, "Join-team")
    }

    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        when (`object`.id) {
            42031 -> {
                TeleportationInterfacePlugin.displaySelectionInterface(player, true)
            }
        }
        return true
    }
}
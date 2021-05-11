package plugin.minigame

import game.content.plugin.type.ObjectPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.player.Player
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
package plugin.rsinterface

import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player

class SupportInterfacePlugin : InterfacePlugin {
    override fun register() {
        registerInterfacePlugin(1019)
    }

    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        return true
    }
}
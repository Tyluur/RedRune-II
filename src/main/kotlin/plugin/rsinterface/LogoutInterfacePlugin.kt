package plugin.rsinterface

import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class LogoutInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int,
    ): Boolean {
        if (player.interfaceManager.containsInventoryInter()) {
            return true
        }
        if (componentId == 6 || componentId == 13) {
            if (player.isFinished) {
                return true
            }
            player.logout(componentId == 6)
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(182)
    }
}
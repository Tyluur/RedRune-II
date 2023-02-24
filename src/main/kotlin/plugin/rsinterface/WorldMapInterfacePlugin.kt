package plugin.rsinterface

import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class WorldMapInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int,
    ): Boolean {
        if (componentId == 44) {
            player.nextAnimation = Animation(-1)
        }
        player.packets.sendWindowsPane(if (player.interfaceManager.hasRezizableScreen()) 746 else 548, 2)
        return true
    }

    override fun register() {
        registerInterfacePlugin(755)
    }
}
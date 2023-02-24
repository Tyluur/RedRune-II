package plugin.rsinterface

import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class CorporealBeastInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int,
    ): Boolean {
        if (componentId == 17) {
            player.stopAll()
            player.setNextWorldTile(WorldTile(2974, 4384, 0))
            player.controllerManager.startController("CorpBeastController")
        } else if (componentId == 18) {
            player.closeInterfaces()
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(650)
    }
}
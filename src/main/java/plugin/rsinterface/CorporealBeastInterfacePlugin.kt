package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import game.global.WorldTile
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class CorporealBeastInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
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
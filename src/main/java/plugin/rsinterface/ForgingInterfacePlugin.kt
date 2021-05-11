package plugin.rsinterface

import game.content.entity.actor.player.skills.smithing.Smithing.ForgingInterface
import game.content.plugin.type.InterfacePlugin
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class ForgingInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        ForgingInterface.handleIComponents(player, componentId)
        return true
    }

    override fun register() {
        registerInterfacePlugin(300)
    }
}
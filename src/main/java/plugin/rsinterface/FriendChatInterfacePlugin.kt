package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class FriendChatInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        player.contactManager.handleFriendChatButtons(interfaceId, componentId, packetId)
        return true
    }

    override fun register() {
        registerInterfacePlugin(1108, 1109, 1110)
    }
}
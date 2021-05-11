package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class ChatSetupInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        when (componentId) {
            5 -> {
                player.interfaceManager.sendSettings()
            }
            42 -> {
                player.attributes.privateChatSetup = if (player.attributes.privateChatSetup == 0) 1 else 0
            }
            in 49..61 -> {
                player.attributes.privateChatSetup = componentId - 48
            }
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(982)
    }
}
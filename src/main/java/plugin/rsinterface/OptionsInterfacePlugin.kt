package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class OptionsInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        if (player.interfaceManager.containsInventoryInter()) {
            return true
        }
        if (componentId == 14) {
            if (player.interfaceManager.containsScreenInter()) {
                player.packets.sendMessage("Please close the interface you have open before setting your graphic options.")
                return true
            }
            player.stopAll()
            player.interfaceManager.sendInterface(742)
        } else if (componentId == 3) {
            player.attributes.isFilteringProfanity = !player.attributes.isFilteringProfanity
            player.packets.sendProfanityFilterConfig()
        } else if (componentId == 4) {
            player.packets.switchAllowChatEffects()
        } else if (componentId == 5) {
            player.interfaceManager.sendSettings(982)
        } else if (componentId == 6) {
            player.packets.switchMouseButtons()
        } else if (componentId == 16) {
            if (player.interfaceManager.containsScreenInter()) {
                player.packets.sendMessage("Please close the interface you have open before setting your audio options.")
                return true
            }
            player.stopAll()
            player.interfaceManager.sendInterface(743)
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(261)
    }
}
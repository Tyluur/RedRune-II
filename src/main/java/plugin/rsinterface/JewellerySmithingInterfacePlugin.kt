package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import game.content.entity.actor.player.skills.crafting.JewelrySmithing
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class JewellerySmithingInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        JewelrySmithing.handleButtonClick(player, componentId, if (packetId == 14) 1 else if (packetId == 67) 5 else 10)
        return true
    }

    override fun register() {
        registerInterfacePlugin(675)
    }
}
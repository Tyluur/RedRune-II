package plugin.rsinterface

import org.redrune.game.content.entity.actor.player.PlayerLook
import org.redrune.game.content.entity.actor.player.design.PlayerDesign
import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class AppearanceCustomizationInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        if (interfaceId == 1028) {
            PlayerDesign.handle(player, componentId, slotId)
        } else if (interfaceId == 900) {
            PlayerLook.handleMageMakeOverButtons(player, componentId)
        } else if (interfaceId == 309) {
            PlayerLook.handleHairdresserSalonButtons(player, componentId, slotId)
        } else if (interfaceId == 729) {
            PlayerLook.handleThessaliasMakeOverButtons(player, componentId, slotId)
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(1028, 900, 309, 729)
    }
}
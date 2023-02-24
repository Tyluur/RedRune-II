package plugin.rsinterface

import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class EmoteInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int,
    ): Boolean {
        player.emotesManager.useBookEmote(componentId)
        return true
    }

    override fun register() {
        registerInterfacePlugin(464)
    }
}
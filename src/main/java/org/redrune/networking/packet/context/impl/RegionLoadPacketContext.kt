package org.redrune.networking.packet.context.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.context.PacketContext

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class RegionLoadPacketContext : PacketContext() {
    override fun handle(player: Player) {
        if (!player.attributes.clientHasLoadedMapRegion()) {
            player.attributes.setClientHasLoadedMapRegion()
        }
        player.packets.refreshSpawnedObjects()
        player.packets.refreshSpawnedItems()
    }
}
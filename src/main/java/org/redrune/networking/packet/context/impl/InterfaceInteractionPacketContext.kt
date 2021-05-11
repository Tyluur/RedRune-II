package org.redrune.networking.packet.context.impl

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.content.plugin.PluginRepository.handleInterface
import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.context.PacketContext

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-02-04
 */
class InterfaceInteractionPacketContext(
    private val interfaceId: Int,
    private val componentId: Int,
    private val itemId: Int,
    private val slotId: Int,
    private val packetId: Int
) : PacketContext() {

    override fun handle(player: Player) {
        if (!player.controllerManager.processButtonClick(interfaceId, componentId, slotId, packetId)) {
            return
        }
        val plugin = handleInterface(player, interfaceId, componentId, itemId, slotId, packetId)

        if (plugin != null) {
            logger.debug { ("[" + plugin.javaClass.simpleName + "] handled [" + interfaceId + ", " + componentId + ", " + packetId + "]") }
        } else {
            logger.debug { ("[N/A] handled [$interfaceId, $componentId, $packetId]") }
        }
    }


    companion object {

        private val logger = InlineLogger()
    }
}
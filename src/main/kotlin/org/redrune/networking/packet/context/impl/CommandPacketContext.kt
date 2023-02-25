package org.redrune.networking.packet.context.impl

import org.redrune.game.content.plugin.PluginRepository.handleCommand
import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.context.PacketContext
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class CommandPacketContext(
    private val clientCommand: Boolean,
    private val unknown: Boolean,
    private val command: String,
) : PacketContext() {
    override fun handle(player: Player) {
        handleCommand(
            player,
            command.lowercase(Locale.getDefault()).replaceFirst("::".toRegex(), "").split(" ").toTypedArray(),
            true,
            clientCommand
        )
    }
}
package org.redrune.networking.packet.incoming.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.context.impl.DialogueChatPacketContext
import org.redrune.networking.packet.incoming.IncomingPacketReader
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.InputEvent

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class DialoguePacketReader : IncomingPacketReader {

    override fun bindings(): IntArray {
        return arguments(
            PacketConstants.DIALOGUE_CONTINUE_PACKET,
            PacketConstants.ENTER_STRING_PACKET,
            PacketConstants.ENTER_LONG_STRING_PACKET,
            PacketConstants.ENTER_INTEGER_PACKET
        )
    }

    override fun read(player: Player, stream: Packet): PacketContext? {
        when (stream.opcode) {
            PacketConstants.DIALOGUE_CONTINUE_PACKET -> {
                val interfaceHash = stream.readIntV2()
                @Suppress("unused") val junk = stream.readShortLE128()
                val interfaceId = interfaceHash shr 16
                @Suppress("unused") val buttonId = interfaceHash and 0xFF
                if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
                    return null
                }
                if (!player.isRunning || !player.interfaceManager.containsInterface(interfaceId)) {
                    return null
                }
                val componentId = interfaceHash - (interfaceId shl 16)
                return DialogueChatPacketContext(interfaceId, componentId)
            }

            PacketConstants.ENTER_STRING_PACKET -> {
                if (!player.isRunning || player.isDead) {
                    return null
                }
                val value = stream.readRS2String()
                if (value == "") {
                    return null
                }
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        if (player.getTemporaryAttribute<Any?>("input_event", null) != null) {
                            val event = player.removeTemporaryAttribute<InputEvent>("input_event")
                            event.setInput(value)
                            event.handleInput()
                            return
                        }
                        if (player.interfaceManager.containsInterface(1108)) {
                            player.contactManager.setChatPrefix(value)
                        }
                    }
                }
            }

            PacketConstants.ENTER_LONG_STRING_PACKET -> {
                val value = stream.readRS2String()
                if (value == "") {
                    return null
                }
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        if (player.getTemporaryAttribute<Any?>("input_event") != null) {
                            val event = player.removeTemporaryAttribute<InputEvent>("input_event")
                            event.setInput(value)
                            event.handleInput()
                        }
                    }
                }
            }

            PacketConstants.ENTER_INTEGER_PACKET -> {
                if (!player.isRunning || player.isDead) {
                    return null
                }
                val value = stream.readInt()
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        if (player.getTemporaryAttribute<Any?>("input_event") != null) {
                            val event = player.removeTemporaryAttribute<InputEvent>("input_event")
                            event.setInput(value)
                            event.handleInput()
                        }
                    }
                }
            }
        }
        return null
    }
}
package org.redrune.networking.packet.incoming.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.context.impl.ClientFramePacketContext
import org.redrune.networking.packet.incoming.IncomingPacketReader
import org.redrune.utility.constants.PacketConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class ClientFramePacketReader : IncomingPacketReader {
    override fun bindings(): IntArray {
        return arguments(
            PacketConstants.MOVE_MOUSE_PACKET,
            PacketConstants.KEY_TYPED_PACKET,
            PacketConstants.MOVE_CAMERA_PACKET,
            PacketConstants.CLICK_PACKET,
            PacketConstants.WINDOW_SWITCH_PACKET
        )
    }

    override fun read(player: Player, packet: Packet): PacketContext {
        val packetId = packet.opcode
        when (packetId) {
            PacketConstants.MOVE_MOUSE_PACKET -> {
            }
            PacketConstants.KEY_TYPED_PACKET -> {
                val keyCode = packet.readByte().toInt()
                when (keyCode) {
                    16 -> {
                        // 1
                        val optionComponent =
                            player.interfaceManager.getDialogueInterfaceDefinitions("option1")
                        optionComponent?.let {
                            player.dialogueManager.continueDialogue(
                                player.interfaceManager.chatboxInterface,
                                it.widgetId
                            )
                        }
                    }
                    17 -> {
                        // 2
                        val optionComponent =
                            player.interfaceManager.getDialogueInterfaceDefinitions("option2")
                        if (optionComponent != null) {
                            player.dialogueManager.continueDialogue(
                                player.interfaceManager.chatboxInterface,
                                optionComponent.widgetId
                            )
                        }
                    }
                    18 -> {
                        // 3
                        val optionComponent =
                            player.interfaceManager.getDialogueInterfaceDefinitions("option3")
                        if (optionComponent != null) {
                            player.dialogueManager.continueDialogue(
                                player.interfaceManager.chatboxInterface,
                                optionComponent.widgetId
                            )
                        }
                    }
                    19 -> {
                        // 4
                        val optionComponent =
                            player.interfaceManager.getDialogueInterfaceDefinitions("option4")
                        if (optionComponent != null) {
                            player.dialogueManager.continueDialogue(
                                player.interfaceManager.chatboxInterface,
                                optionComponent.widgetId
                            )
                        }
                    }
                    20 -> {
                        // 5
                        val optionComponent =
                            player.interfaceManager.getDialogueInterfaceDefinitions("option5")
                        if (optionComponent != null) {
                            player.dialogueManager.continueDialogue(
                                player.interfaceManager.chatboxInterface,
                                optionComponent.widgetId
                            )
                        }
                    }
                    13 -> player.closeInterfaces()
                    83 -> {
                        val continueComponent =
                            player.interfaceManager.getDialogueInterfaceDefinitions("Click here to continue")
                        if (continueComponent != null) {
                            player.dialogueManager.continueDialogue(
                                player.interfaceManager.chatboxInterface,
                                continueComponent.widgetId
                            )
                        }
                    }
                }
            }
            PacketConstants.MOVE_CAMERA_PACKET -> {
            }
            PacketConstants.CLICK_PACKET -> {
                val mouseHash = packet.readShortLE128()
                val mouseButton = mouseHash shr 15
                val time = mouseHash - (mouseButton shl 15) // time
                val positionHash = packet.readIntV1()
                val y = positionHash shr 16 // y;
                val x = positionHash - (y shl 16) // x
                var clicked: Boolean
                // mass click or stupid autoclicker, lets stop lagg
                if (time <= 1 || x < 0 || x > player.interfaceManager.screenWidth || y < 0 || y > player.interfaceManager.screenHeight) {
                    // player.getSession().getChannel().close();
                    clicked = false
                    return ClientFramePacketContext()
                }
                clicked = true
            } // true if we swap to client, false if client is in backgrond
            PacketConstants.WINDOW_SWITCH_PACKET -> {
                var dominant: Boolean = packet.readByte().toInt() == 1
            }
        }
        return ClientFramePacketContext()
    }
}
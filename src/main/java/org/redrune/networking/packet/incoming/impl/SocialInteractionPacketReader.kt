package org.redrune.networking.packet.incoming.impl

import com.alex.io.InputStream
import com.github.michaelbull.logging.InlineLogger
import org.redrune.cache.huffman.Huffman
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.actor.player.link.FriendChatsManager.Companion.joinChat
import org.redrune.game.global.World
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.context.impl.ChatPacketContext
import org.redrune.networking.packet.context.impl.CommandPacketContext
import org.redrune.networking.packet.incoming.IncomingPacketReader
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.entity.actor.player.QuickChatMessage

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-02-04
 */
class SocialInteractionPacketReader : IncomingPacketReader {

    override fun bindings(): IntArray {
        return arguments(
            PacketConstants.JOIN_FRIEND_CHAT_PACKET,
            PacketConstants.KICK_FRIEND_CHAT_PACKET,
            PacketConstants.CHANGE_FRIEND_CHAT_PACKET,
            PacketConstants.ADD_FRIEND_PACKET,
            PacketConstants.REMOVE_FRIEND_PACKET,
            PacketConstants.SEND_FRIEND_MESSAGE_PACKET,
            PacketConstants.SEND_FRIEND_QUICK_CHAT_PACKET,
            PacketConstants.PUBLIC_QUICK_CHAT_PACKET,
            PacketConstants.CHAT_TYPE_PACKET,
            PacketConstants.CHAT_PACKET
        )
    }

    override fun read(player: Player, stream: Packet): PacketContext {
        val packetId = stream.opcode
        val packetLength = stream.length
        when (packetId) {
            PacketConstants.JOIN_FRIEND_CHAT_PACKET -> {
                if (player.hasStarted()) {
                    val name = stream.readRS2String()
                    return object : PacketContext() {
                        override fun handle(player: Player) {
                            joinChat(name, player)
                        }
                    }
                }
            }


            PacketConstants.KICK_FRIEND_CHAT_PACKET -> {
                if (player.hasStarted()) {
                    val name = stream.readRS2String()
                    player.attributes.lastPublicMessage = Misc.currentTimeMillis() + 1000
                    return object : PacketContext() {
                        override fun handle(player: Player) {
                            player.currentFriendChat.kickPlayerFromFriendsChannel(name, player)
                        }
                    }
                }
            }

            PacketConstants.CHANGE_FRIEND_CHAT_PACKET -> {
                if (player.hasStarted() && player.interfaceManager.containsInterface(1108)) {
                    val name = stream.readRS2String()
                    val rank = stream.readUnsignedByteC()
                    return object : PacketContext() {
                        override fun handle(player: Player) {
                            player.contactManager.changeRank(name, rank)
                        }
                    }
                }
            }

            PacketConstants.ADD_FRIEND_PACKET -> {
                if (player.hasStarted()) {
                    val name = stream.readRS2String()
                    return object : PacketContext() {
                        override fun handle(player: Player) {
                            player.contactManager.addFriend(name)
                        }
                    }
                }
            }

            PacketConstants.REMOVE_FRIEND_PACKET -> {
                if (player.hasStarted()) {
                    val name = stream.readRS2String()
                    return object : PacketContext() {
                        override fun handle(player: Player) {
                            player.contactManager.removeFriend(name)
                        }
                    }
                }
            }

            PacketConstants.SEND_FRIEND_MESSAGE_PACKET -> {
                if (player.hasStarted()) {
                    val `is` = InputStream(stream.buffer.array())
                    val username = `is`.readString()
                    val p2 = World.getPlayerByDisplayName(username) ?: null
                    return object : PacketContext() {
                        override fun handle(player: Player) {
                            val message = Huffman.readEncryptedMessage(150, `is`)
                            println(message)
                            player.contactManager.sendMessage(p2, Misc.fixChatMessage(message))
                        }
                    }
                }
            }

            PacketConstants.SEND_FRIEND_QUICK_CHAT_PACKET -> {
                if (player.hasStarted()) {
                    val username = stream.readRS2String()
                    val fileId = stream.readUnsignedShort()
                    var data: ByteArray? = null
                    if (packetLength > 3 + username.length) {
                        data = ByteArray(packetLength - (3 + username.length))
                        stream.readBytes(data)
                    }
                    data = Misc.completeQuickMessage(player, fileId, data)
                    val p2 = World.getPlayerByDisplayName(username) ?: null
                    val quickChatMessage = QuickChatMessage(fileId, data)
                    return object : PacketContext() {
                        override fun handle(player: Player) {
                            player.contactManager.sendQuickChatMessage(p2, quickChatMessage)
                        }
                    }
                }
            }

            PacketConstants.PUBLIC_QUICK_CHAT_PACKET -> {
                if (player.hasStarted()) {
                    player.attributes.lastPublicMessage = Misc.currentTimeMillis() + 300
                    // just tells you which client script created packet
                    @Suppress("unused") val secondClientScript = stream.readByte().toInt() == 1 // script 5059

                    // or 5061
                    val fileId = stream.readUnsignedShort()
                    var data: ByteArray? = null
                    val chatType = player.getTemporaryAttribute("chatType", 0)
                    if (packetLength > 3) {
                        data = ByteArray(packetLength - 3)
                        stream.readBytes(data)
                    }
                    data = Misc.completeQuickMessage(player, fileId, data)
                    val message = QuickChatMessage(fileId, data)
                    return object : PacketContext() {
                        override fun handle(player: Player) {
                            if (chatType == 0) {
                                player.sendPublicChatMessage(message)
                            } else if (chatType == 1) {
                                player.currentFriendChat.sendFriendsChannelQuickMessage(message, player)
                            } else {
                                println("Unknown chat type: $chatType")
                            }
                        }
                    }
                }
            }

            PacketConstants.CHAT_TYPE_PACKET -> {
                val chatType = stream.readUnsignedByte()
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        player.putTemporaryAttribute("chatType", chatType)
                    }
                }
            }

            PacketConstants.CHAT_PACKET -> {
                if (player.hasStarted()) {
                    val stream = InputStream(stream.buffer.array())
                    val colorEffect = stream.readUnsignedByte()
                    val moveEffect = stream.readUnsignedByte()
                    val message = Huffman.readEncryptedMessage(250, stream).replace(" ".toRegex(), "")
                    return if (message.startsWith("::") || message.startsWith(";;")) {
                        CommandPacketContext(
                            false,
                            false,
                            message.replaceFirst("::".toRegex(), "").replaceFirst(";;".toRegex(), "").toLowerCase()
                        )
                    } else ChatPacketContext(colorEffect, moveEffect, message)
                }
            }
        }
        return object : PacketContext() {
            override fun handle(player: Player) {
                logger.debug { "Unexpected packet context case" }
            }
        }
    }

    private val logger = InlineLogger()

}
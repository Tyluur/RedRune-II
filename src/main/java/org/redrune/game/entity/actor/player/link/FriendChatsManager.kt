package org.redrune.game.entity.actor.player.link

import com.alex.io.OutputStream
import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import org.redrune.utility.constants.GameConstants
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.entity.actor.player.ChatMessage
import org.redrune.utility.game.entity.actor.player.JacksonFactory.fromFile
import org.redrune.utility.game.entity.actor.player.JacksonFactory.playerExists
import org.redrune.utility.game.entity.actor.player.QuickChatMessage
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class FriendChatsManager private constructor(player: Player) {
    val ownerName: String
    val ownerDisplayName: String
    private var settings: ContactManager
    val players: CopyOnWriteArrayList<Player>
    private val bannedPlayers: ConcurrentHashMap<String, Long>
    var dataBlock: ByteArray? = null
        private set

    fun destroyChat() {
        synchronized(this) {
            for (player in players) {
                player.currentFriendChat = null
                player.attributes.currentFriendChatOwner = null
                player.packets.sendFriendsChatChannel()
                player.packets.sendMessage("You have been removed from this channel!")
            }
        }
        synchronized(cachedFriendChats!!) {
            cachedFriendChats!!.remove(
                ownerName
            )
        }
    }

    private fun refreshChannel() {
        synchronized(this) {
            val stream = OutputStream()
            stream.writeString(ownerDisplayName)
            val ownerName = Misc.formatPlayerNameForDisplay(ownerName)
            stream.writeByte(if (ownerDisplayName == ownerName) 0 else 1)
            if (ownerDisplayName != ownerName) {
                stream.writeString(ownerName)
            }
            stream.writeLong(Misc.stringToLong(channelName))
            val kickOffset = stream.offset
            stream.writeByte(0)
            stream.writeByte(players.size)
            for (player in players) {
                val displayName = player.displayName
                val name = Misc.formatPlayerNameForDisplay(player.username)
                stream.writeString(displayName)
                stream.writeByte(if (displayName == name) 0 else 1)
                if (displayName != name) {
                    stream.writeString(name)
                }
                stream.writeShort(1)
                val rank = getRank(player.username)
                stream.writeByte(rank)
                stream.writeString(GameConstants.SERVER_NAME)
            }
            dataBlock = ByteArray(stream.offset)
            stream.offset = 0
            stream.getBytes(dataBlock, 0, dataBlock!!.size)
            for (player in players) {
                dataBlock!![kickOffset] = (if (player.username == this.ownerName) 0 else whoCanKickOnChat).toByte()
                player.packets.sendFriendsChatChannel()
            }
        }
    }

    val channelName: String
        get() = settings.chatName.replace("<img=".toRegex(), "")

    fun getRank(username: String): Int {
        return if (username == ownerName) {
            7
        } else settings.getRank(username)
    }

    val whoCanKickOnChat: Int
        get() = settings.whoCanKickOnChat

    private fun joinChat(player: Player) {
        synchronized(this) {
            if (player.username != ownerName && !settings.hasRankToJoin(player.username)) {
                player.packets.sendMessage("You do not have a enough rank to join this friends chat channel.")
                return
            }
            if (players.size >= 100) {
                player.packets.sendMessage("This chat is full.")
                return
            }
            val bannedSince = bannedPlayers[player.username]
            if (bannedSince != null) {
                if (bannedSince + 3600000 > Misc.currentTimeMillis()) {
                    player.packets.sendMessage("You have been banned from this channel.")
                    return
                }
                bannedPlayers.remove(player.username)
            }
            joinChatNoCheck(player)
        }
    }

    fun leaveChat(player: Player, logout: Boolean) {
        synchronized(this) {
            player.currentFriendChat = null
            players.remove(player)
            if (players.size == 0) {
                synchronized(cachedFriendChats!!) {
                    cachedFriendChats!!.remove(
                        ownerName
                    )
                }
            } else {
                refreshChannel()
            }
            if (!logout) {
                player.attributes.currentFriendChatOwner = null
                player.packets.sendMessage("You have left the channel.")
                player.packets.sendFriendsChatChannel()
            }
        }
    }

    private fun joinChatNoCheck(player: Player) {
        synchronized(this) {
            players.add(player)
            player.currentFriendChat = this
            player.attributes.currentFriendChatOwner = ownerName
            player.packets.sendMessage("You are now talking in the friends chat channel " + settings.chatName)
            player.packets.sendMessage("To talk, start each line of chat with the / symbol.")
            refreshChannel()
        }
    }

    fun kickPlayerFromFriendsChannel(name: String, player: Player) {
        kickPlayerFromChat(player, name)
    }

    fun kickPlayerFromChat(player: Player, username: String) {
        val name = StringBuilder()
        for (character in username.toCharArray()) {
            name.append(if (Misc.containsInvalidCharacter(character)) " " else character)
        }
        synchronized(this) {
            val rank = getRank(player.username)
            if (rank < whoCanKickOnChat) {
                return
            }
            val kicked = getPlayerByDisplayName(name.toString())
            if (kicked == null) {
                player.packets.sendMessage("This player is not this channel.")
                return
            }
            if (rank <= getRank(kicked.username)) {
                return
            }
            kicked.currentFriendChat = null
            kicked.attributes.currentFriendChatOwner = null
            players.remove(kicked)
            bannedPlayers[kicked.username] = Misc.currentTimeMillis()
            kicked.packets.sendFriendsChatChannel()
            kicked.packets.sendMessage("You have been kicked from the friends chat channel.")
            player.packets.sendMessage("You have kicked " + kicked.username + " from friends chat channel.")
            refreshChannel()
        }
    }

    fun getPlayerByDisplayName(username: String): Player? {
        val formatedUsername = Misc.formatPlayerNameForProtocol(username)
        for (player in players) {
            if (player.username == formatedUsername || player.displayName == username) {
                return player
            }
        }
        return null
    }

    fun sendFriendsChannelMessage(message: ChatMessage, player: Player) {
        sendMessage(player, message)
    }

    private fun sendMessage(player: Player, message: ChatMessage) {
        synchronized(this) {
            if (player.username != ownerName && !settings.canTalk(player)) {
                player.packets.sendMessage("You do not have a enough rank to talk on this friends chat channel.")
                return
            }
            val formattedName = Misc.formatPlayerNameForDisplay(player.username)
            val displayName = player.displayName
            val rights = player.messageIcon
            for (p2 in players) {
                p2.packets.receiveFriendChatMessage(formattedName, displayName, rights, settings.chatName, message)
            }
        }
    }

    fun sendFriendsChannelQuickMessage(message: QuickChatMessage?, player: Player) {
        sendQuickMessage(player, message)
    }

    fun sendQuickMessage(player: Player, message: QuickChatMessage?) {
        synchronized(this) {
            if (player.username != ownerName && !settings.canTalk(player)) {
                player.packets.sendMessage("You do not have a enough rank to talk on this friends chat channel.")
                return
            }
            val formatedName = Misc.formatPlayerNameForDisplay(player.username)
            val displayName = player.displayName
            val rights = player.messageIcon
            for (p2 in players) {
                p2.packets.receiveFriendChatQuickMessage(formatedName, displayName, rights, settings.chatName, message)
            }
        }
    }

    companion object {
        private var cachedFriendChats: HashMap<String, FriendChatsManager>? = null
        fun initialize() {
            cachedFriendChats = HashMap()
            logger.info { "Loaded " + cachedFriendChats!!.size + " cached friends chats" }
        }

        fun destroyChat(player: Player) {
            synchronized(cachedFriendChats!!) {
                val chat = cachedFriendChats!![player.username] ?: return
                chat.destroyChat()
                player.packets.sendMessage("Your friends chat channel has now been disabled!")
            }
        }

        @kotlin.jvm.JvmStatic
        fun linkSettings(player: Player) {
            synchronized(cachedFriendChats!!) {
                val chat = cachedFriendChats!![player.username] ?: return
                chat.settings = player.contactManager
            }
        }

        @kotlin.jvm.JvmStatic
        fun refreshChat(player: Player) {
            synchronized(cachedFriendChats!!) {
                val chat = cachedFriendChats!![player.username] ?: return
                chat.refreshChannel()
            }
        }

        fun joinChat(ownerName: String, player: Player) {
            synchronized(cachedFriendChats!!) {
                if (player.currentFriendChat != null) {
                    return
                }
                player.packets.sendMessage("Attempting to join channel...", true)
                val formatedName = Misc.formatPlayerNameForProtocol(ownerName)
                var chat = cachedFriendChats!![formatedName]
                if (chat == null) {
                    var owner = World.getPlayerByDisplayName(ownerName)
                    if (owner == null) {
                        owner = World.getPlayerByDisplayName(ownerName)
                    }
                    if (owner == null) {
                        if (!playerExists(formatedName)) {
                            player.packets.sendMessage("The channel you tried to join does not exist.", true)
                            return
                        }
                        owner = fromFile(formatedName)
                        if (owner == null) {
                            player.packets.sendMessage("The channel you tried to join does not exist.", true)
                            return
                        }
                        owner.username = formatedName
                    }
                    val settings = owner.contactManager
                    if (!settings.hasFriendChat()) {
                        player.packets.sendMessage("The channel you tried to join does not exist.", true)
                        return
                    }
                    if (player.username != ownerName && !settings.hasRankToJoin(player.username)) {
                        player.packets.sendMessage("You do not have a enough rank to join this friends chat channel.")
                        return
                    }
                    chat = FriendChatsManager(owner)
                    cachedFriendChats!![ownerName] = chat
                    chat.joinChatNoCheck(player)
                } else {
                    chat.joinChat(player)
                }
            }
        }

        private val logger = InlineLogger()
    }

    init {
        ownerName = player.username
        ownerDisplayName = player.displayName
        settings = player.contactManager
        players = CopyOnWriteArrayList()
        bannedPlayers = ConcurrentHashMap()
    }
}
package org.redrune.networking.packet.context.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.punishment.PunishmentRepository.isPunished
import org.redrune.game.global.punishment.PunishmentType
import org.redrune.networking.packet.context.PacketContext
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.entity.actor.player.ChatMessage
import org.redrune.utility.game.entity.actor.player.PublicChatMessage

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class ChatPacketContext(private val colorEffect: Int, private val moveEffect: Int, private val message: String) :
    PacketContext() {
    override fun handle(player: Player) {
        if (isPunished(player, PunishmentType.PLAYER_MUTE, PunishmentType.ADDRESS_MUTE)) {
            player.packets.sendMessage("You are muted.")
            return
        }
        val effects = colorEffect shl 8 or (moveEffect and 0xff)
        val chatType = player.getTemporaryAttribute("chatType", 0)
        if (chatType == 1) {
            player.currentFriendChat.sendFriendsChannelMessage(ChatMessage(message), player)
        } else {
            player.sendPublicChatMessage(PublicChatMessage(Misc.fixChatMessage(message), effects))
        }
        player.attributes.lastPublicMessage = Misc.currentTimeMillis() + 300
    }
}
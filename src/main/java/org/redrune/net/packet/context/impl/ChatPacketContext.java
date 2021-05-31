package org.redrune.net.packet.context.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.punishment.PunishmentRepository;
import org.redrune.game.global.punishment.PunishmentType;
import org.redrune.net.packet.context.PacketContext;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.entity.actor.player.ChatMessage;
import org.redrune.utility.game.entity.actor.player.PublicChatMessage;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class ChatPacketContext extends PacketContext {
	
	private final int colorEffect;
	private final int moveEffect;
	private final String message;
	
	public ChatPacketContext(int colorEffect, int moveEffect, String message) {
		this.colorEffect = colorEffect;
		this.moveEffect = moveEffect;
		this.message = message;
	}
	
	@Override
	public void handle(Player player) {
		if (PunishmentRepository.isPunished(player, PunishmentType.PLAYER_MUTE, PunishmentType.ADDRESS_MUTE)) {
			player.getPackets().sendMessage("You are muted.");
			return;
		}
		int effects = (colorEffect << 8) | (moveEffect & 0xff);
		int chatType = player.getTemporaryAttribute("chatType", 0);
		if (chatType == 1) {
			player.getCurrentFriendChat().sendFriendsChannelMessage(new ChatMessage(message), player);
		} else {
			player.sendPublicChatMessage(new PublicChatMessage(Misc.fixChatMessage(message), effects));
		}
		player.getAttributes().setLastPublicMessage(Misc.currentTimeMillis() + 300);
	}
}

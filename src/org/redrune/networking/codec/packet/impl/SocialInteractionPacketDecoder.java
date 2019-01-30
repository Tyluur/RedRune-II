package org.redrune.networking.codec.packet.impl;

import org.redrune.cache.huffman.Huffman;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.link.FriendChatsManager;
import org.redrune.game.global.World;
import org.redrune.game.global.punishment.PunishmentRepository;
import org.redrune.game.global.punishment.PunishmentType;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.entity.actor.player.ChatMessage;
import org.redrune.utility.game.entity.actor.player.PublicChatMessage;
import org.redrune.utility.game.entity.actor.player.QuickChatMessage;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class SocialInteractionPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(JOIN_FRIEND_CHAT_PACKET, KICK_FRIEND_CHAT_PACKET, CHANGE_FRIEND_CHAT_PACKET, ADD_FRIEND_PACKET, REMOVE_FRIEND_PACKET, SEND_FRIEND_MESSAGE_PACKET, SEND_FRIEND_QUICK_CHAT_PACKET, PUBLIC_QUICK_CHAT_PACKET, CHAT_TYPE_PACKET, CHAT_PACKET);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
		switch (packetId) {
			case JOIN_FRIEND_CHAT_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				FriendChatsManager.joinChat(stream.readString(), player);
				break;
			case KICK_FRIEND_CHAT_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				player.getAttributes().setLastPublicMessage(Misc.currentTimeMillis() + 1000);
				player.getCurrentFriendChat().kickPlayerFromFriendsChannel(stream.readString(), player);
				break;
			case CHANGE_FRIEND_CHAT_PACKET:
				if (!player.hasStarted() || !player.getInterfaceManager().containsInterface(1108)) {
					return;
				}
				player.getContactManager().changeRank(stream.readString(), stream.readUnsignedByteC());
				break;
			case ADD_FRIEND_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				player.getContactManager().addFriend(stream.readString());
				break;
			case REMOVE_FRIEND_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				player.getContactManager().removeFriend(stream.readString());
				break;
			case SEND_FRIEND_MESSAGE_PACKET: {
				if (!player.hasStarted()) {
					return;
				}
				String username = stream.readString();
				Player p2 = World.getPlayerByDisplayName(username);
				if (p2 == null) {
					return;
				}
				player.getContactManager().sendMessage(p2, Misc.fixChatMessage(Huffman.readEncryptedMessage(150, stream)));
				break;
			}
			case SEND_FRIEND_QUICK_CHAT_PACKET: {
				if (!player.hasStarted()) {
					return;
				}
				String username = stream.readString();
				int fileId = stream.readUnsignedShort();
				byte[] data = null;
				if (packetLength > 3 + username.length()) {
					data = new byte[packetLength - (3 + username.length())];
					stream.readBytes(data);
				}
				data = Misc.completeQuickMessage(player, fileId, data);
				Player p2 = World.getPlayerByDisplayName(username);
				if (p2 == null) {
					return;
				}
				player.getContactManager().sendQuickChatMessage(p2, new QuickChatMessage(fileId, data));
				break;
			}
			case PUBLIC_QUICK_CHAT_PACKET: {
				if (!player.hasStarted()) {
					return;
				}
				if (player.getAttributes().getLastPublicMessage() > Misc.currentTimeMillis()) {
					return;
				}
				if (PunishmentRepository.isPunished(player, PunishmentType.PLAYER_MUTE, PunishmentType.ADDRESS_MUTE)) {
					player.getPackets().sendGameMessage("You are muted.");
					return;
				}
				player.getAttributes().setLastPublicMessage(Misc.currentTimeMillis() + 300);
				// just tells you which client script created packet
				@SuppressWarnings("unused") boolean secondClientScript = stream.readByte() == 1;// script 5059
				
				// or 5061
				int fileId = stream.readUnsignedShort();
				byte[] data = null;
				int chatType = player.getTemporaryAttribute("chatType", 0);
				if (packetLength > 3) {
					data = new byte[packetLength - 3];
					stream.readBytes(data);
				}
				data = Misc.completeQuickMessage(player, fileId, data);
				if (chatType == 0) {
					player.sendPublicChatMessage(new QuickChatMessage(fileId, data));
				} else if (chatType == 1) {
					player.getCurrentFriendChat().sendFriendsChannelQuickMessage(new QuickChatMessage(fileId, data), player);
				} else {
					System.out.println("Unknown chat type: " + chatType);
				}
				break;
			}
			case CHAT_TYPE_PACKET: {
				int chatType = stream.readUnsignedByte();
				player.putAttribute("chatType", chatType);
				break;
			}
			case CHAT_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				if (player.getAttributes().getLastPublicMessage() > Misc.currentTimeMillis()) {
					return;
				}
				
				player.getAttributes().setLastPublicMessage(Misc.currentTimeMillis() + 300);
				int colorEffect = stream.readUnsignedByte();
				int moveEffect = stream.readUnsignedByte();
				String message = Huffman.readEncryptedMessage(250, stream);
				if (message == null || message.replaceAll(" ", "").equals("")) {
					return;
				}
				if (message.startsWith("::")) {
					PluginRepository.handleCommand(player, message.replaceFirst("::", "").split(" "), false, false);
					return;
				}
				if (PunishmentRepository.isPunished(player, PunishmentType.PLAYER_MUTE, PunishmentType.ADDRESS_MUTE)) {
					player.getPackets().sendGameMessage("You are muted.");
					return;
				}
				int effects = (colorEffect << 8) | (moveEffect & 0xff);
				int chatType = player.getTemporaryAttribute("chatType", 0);
				if (chatType == 1) {
					player.getCurrentFriendChat().sendFriendsChannelMessage(new ChatMessage(message), player);
				} else {
					player.sendPublicChatMessage(new PublicChatMessage(Misc.fixChatMessage(message), effects));
				}
				break;
		}
	}
}

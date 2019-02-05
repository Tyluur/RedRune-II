package org.redrune.networking.packet.incoming.impl;

import com.alex.io.InputStream;
import org.redrune.cache.huffman.Huffman;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.link.FriendChatsManager;
import org.redrune.game.global.World;
import org.redrune.game.global.punishment.PunishmentRepository;
import org.redrune.game.global.punishment.PunishmentType;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.context.impl.ChatPacketContext;
import org.redrune.networking.packet.context.impl.CommandPacketContext;
import org.redrune.networking.packet.incoming.IncomingPacketReader;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.entity.actor.player.QuickChatMessage;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-04
 */
public class SocialInteractionPacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(JOIN_FRIEND_CHAT_PACKET, KICK_FRIEND_CHAT_PACKET, CHANGE_FRIEND_CHAT_PACKET, ADD_FRIEND_PACKET, REMOVE_FRIEND_PACKET, SEND_FRIEND_MESSAGE_PACKET, SEND_FRIEND_QUICK_CHAT_PACKET, PUBLIC_QUICK_CHAT_PACKET, CHAT_TYPE_PACKET, CHAT_PACKET);
	}
	
	@Override
	public PacketContext read(Player player, Packet stream) {
		int packetId = stream.getOpcode();
		int packetLength = stream.getLength();
		switch (packetId) {
			case JOIN_FRIEND_CHAT_PACKET: {
				if (!player.hasStarted()) {
					break;
				}
				final String name = stream.readRS2String();
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						FriendChatsManager.joinChat(name, player);
					}
				};
			}
			case KICK_FRIEND_CHAT_PACKET: {
				if (!player.hasStarted()) {
					break;
				}
				String name = stream.readRS2String();
				player.getAttributes().setLastPublicMessage(Misc.currentTimeMillis() + 1000);
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						player.getCurrentFriendChat().kickPlayerFromFriendsChannel(name, player);
					}
				};
			}
			case CHANGE_FRIEND_CHAT_PACKET: {
				if (!player.hasStarted() || !player.getInterfaceManager().containsInterface(1108)) {
					break;
				}
				String name = stream.readRS2String();
				int rank = stream.readUnsignedByteC();
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						player.getContactManager().changeRank(name, rank);
					}
				};
			}
			case ADD_FRIEND_PACKET: {
				if (!player.hasStarted()) {
					break;
				}
				String name = stream.readRS2String();
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						player.getContactManager().addFriend(name);
					}
				};
			}
			case REMOVE_FRIEND_PACKET: {
				if (!player.hasStarted()) {
					break;
				}
				String name = stream.readRS2String();
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						player.getContactManager().removeFriend(name);
					}
				};
			}
			case SEND_FRIEND_MESSAGE_PACKET: {
				if (!player.hasStarted()) {
					break;
				}
				InputStream is = new InputStream(stream.getBuffer().array());
				final String username = is.readString();
				final Player p2 = World.getPlayerByDisplayName(username);
				if (p2 == null) {
					break;
				}
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						String message = Huffman.readEncryptedMessage(150, is);
						System.out.println(message);
						player.getContactManager().sendMessage(p2, Misc.fixChatMessage(message));
					}
				};
			}
			case SEND_FRIEND_QUICK_CHAT_PACKET: {
				if (!player.hasStarted()) {
					break;
				}
				String username = stream.readRS2String();
				int fileId = stream.readUnsignedShort();
				byte[] data = null;
				if (packetLength > 3 + username.length()) {
					data = new byte[packetLength - (3 + username.length())];
					stream.readBytes(data);
				}
				data = Misc.completeQuickMessage(player, fileId, data);
				Player p2 = World.getPlayerByDisplayName(username);
				if (p2 == null) {
					break;
				}
				final QuickChatMessage quickChatMessage = new QuickChatMessage(fileId, data);
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						player.getContactManager().sendQuickChatMessage(p2, quickChatMessage);
					}
				};
			}
			case PUBLIC_QUICK_CHAT_PACKET: {
				if (!player.hasStarted()) {
					break;
				}
				if (player.getAttributes().getLastPublicMessage() > Misc.currentTimeMillis()) {
					break;
				}
				if (PunishmentRepository.isPunished(player, PunishmentType.PLAYER_MUTE, PunishmentType.ADDRESS_MUTE)) {
					player.getPackets().sendGameMessage("You are muted.");
					break;
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
				final QuickChatMessage message = new QuickChatMessage(fileId, data);
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						if (chatType == 0) {
							player.sendPublicChatMessage(message);
						} else if (chatType == 1) {
							player.getCurrentFriendChat().sendFriendsChannelQuickMessage(message, player);
						} else {
							System.out.println("Unknown chat type: " + chatType);
						}
					}
				};
			}
			case CHAT_TYPE_PACKET: {
				final int chatType = stream.readUnsignedByte();
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						player.putAttribute("chatType", chatType);
					}
				};
			}
			case CHAT_PACKET: {
				if (!player.hasStarted()) {
					break;
				}
				if (player.getAttributes().getLastPublicMessage() > Misc.currentTimeMillis()) {
					break;
				}
				InputStream is = new InputStream(stream.getBuffer().array());
				int colorEffect = is.readUnsignedByte();
				int moveEffect = is.readUnsignedByte();
				String message = Huffman.readEncryptedMessage(250, is);
				if (message.replaceAll(" ", "").equals("")) {
					break;
				}
				if (message.startsWith("::")) {
					return new CommandPacketContext(false, false, message.replaceFirst("::", ""));
				}
				return new ChatPacketContext(colorEffect, moveEffect, message);
			}
		}
		return null;
	}
}

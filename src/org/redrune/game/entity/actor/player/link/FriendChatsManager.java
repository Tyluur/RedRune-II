package org.redrune.game.entity.actor.player.link;

import com.alex.io.OutputStream;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.utility.constants.GameConstants;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.entity.actor.player.ChatMessage;
import org.redrune.utility.game.entity.actor.player.PlayerSaving;
import org.redrune.utility.game.entity.actor.player.QuickChatMessage;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class FriendChatsManager {
	
	private static HashMap<String, FriendChatsManager> cachedFriendChats;
	
	private String owner;
	
	private String ownerDisplayName;
	
	private ContactManager settings;
	
	private CopyOnWriteArrayList<Player> players;
	
	private ConcurrentHashMap<String, Long> bannedPlayers;
	
	private byte[] dataBlock;
	
	private FriendChatsManager(Player player) {
		owner = player.getUsername();
		ownerDisplayName = player.getDisplayName();
		settings = player.getContactManager();
		players = new CopyOnWriteArrayList<>();
		bannedPlayers = new ConcurrentHashMap<>();
	}
	
	public static void initialize() {
		cachedFriendChats = new HashMap<>();
		System.out.println("Loaded " + cachedFriendChats.size() + " cached friends chats");
	}
	
	public static void destroyChat(Player player) {
		synchronized (cachedFriendChats) {
			FriendChatsManager chat = cachedFriendChats.get(player.getUsername());
			if (chat == null) {
				return;
			}
			chat.destroyChat();
			player.getPackets().sendGameMessage("Your friends chat channel has now been disabled!");
		}
	}
	
	public void destroyChat() {
		synchronized (this) {
			for (Player player : players) {
				player.setCurrentFriendChat(null);
				player.getAttributes().setCurrentFriendChatOwner(null);
				player.getPackets().sendFriendsChatChannel();
				player.getPackets().sendGameMessage("You have been removed from this channel!");
			}
		}
		synchronized (cachedFriendChats) {
			cachedFriendChats.remove(owner);
		}
	}
	
	public static void linkSettings(Player player) {
		synchronized (cachedFriendChats) {
			FriendChatsManager chat = cachedFriendChats.get(player.getUsername());
			if (chat == null) {
				return;
			}
			chat.settings = player.getContactManager();
		}
	}
	
	public static void refreshChat(Player player) {
		synchronized (cachedFriendChats) {
			FriendChatsManager chat = cachedFriendChats.get(player.getUsername());
			if (chat == null) {
				return;
			}
			chat.refreshChannel();
		}
	}
	
	private void refreshChannel() {
		synchronized (this) {
			OutputStream stream = new OutputStream();
			stream.writeString(ownerDisplayName);
			String ownerName = Misc.formatPlayerNameForDisplay(owner);
			stream.writeByte(getOwnerDisplayName().equals(ownerName) ? 0 : 1);
			if (!getOwnerDisplayName().equals(ownerName)) {
				stream.writeString(ownerName);
			}
			stream.writeLong(Misc.stringToLong(getChannelName()));
			int kickOffset = stream.getOffset();
			stream.writeByte(0);
			stream.writeByte(getPlayers().size());
			for (Player player : getPlayers()) {
				String displayName = player.getDisplayName();
				String name = Misc.formatPlayerNameForDisplay(player.getUsername());
				stream.writeString(displayName);
				stream.writeByte(displayName.equals(name) ? 0 : 1);
				if (!displayName.equals(name)) {
					stream.writeString(name);
				}
				stream.writeShort(1);
				int rank = getRank(player.getUsername());
				stream.writeByte(rank);
				stream.writeString(GameConstants.SERVER_NAME);
			}
			dataBlock = new byte[stream.getOffset()];
			stream.setOffset(0);
			stream.getBytes(dataBlock, 0, dataBlock.length);
			for (Player player : players) {
				dataBlock[kickOffset] = (byte) (player.getUsername().equals(owner) ? 0 : getWhoCanKickOnChat());
				player.getPackets().sendFriendsChatChannel();
			}
		}
	}
	
	public String getOwnerDisplayName() {
		return ownerDisplayName;
	}
	
	public String getChannelName() {
		return settings.getChatName().replaceAll("<img=", "");
	}
	
	public CopyOnWriteArrayList<Player> getPlayers() {
		return players;
	}
	
	public int getRank(String username) {
		if (username.equals(owner)) {
			return 7;
		}
		return settings.getRank(username);
	}
	
	public int getWhoCanKickOnChat() {
		return settings.getWhoCanKickOnChat();
	}
	
	public static void joinChat(String ownerName, Player player) {
		synchronized (cachedFriendChats) {
			if (player.getCurrentFriendChat() != null) {
				return;
			}
			player.getPackets().sendGameMessage("Attempting to join channel...");
			String formatedName = Misc.formatPlayerNameForProtocol(ownerName);
			FriendChatsManager chat = cachedFriendChats.get(formatedName);
			if (chat == null) {
				Player owner = World.getPlayerByDisplayName(ownerName);
				if (owner == null) {
					owner = World.getPlayerByDisplayName(ownerName);
				}
				if (owner == null) {
					if (!PlayerSaving.playerExists(formatedName)) {
						player.getPackets().sendGameMessage("The channel you tried to join does not exist.");
						return;
					}
					owner = PlayerSaving.fromFile(formatedName);
					if (owner == null) {
						player.getPackets().sendGameMessage("The channel you tried to join does not exist.");
						return;
					}
					owner.setUsername(formatedName);
				}
				ContactManager settings = owner.getContactManager();
				if (!settings.hasFriendChat()) {
					player.getPackets().sendGameMessage("The channel you tried to join does not exist.");
					return;
				}
				if (!player.getUsername().equals(ownerName) && !settings.hasRankToJoin(player.getUsername())) {
					player.getPackets().sendGameMessage("You do not have a enough rank to join this friends chat channel.");
					return;
				}
				chat = new FriendChatsManager(owner);
				cachedFriendChats.put(ownerName, chat);
				chat.joinChatNoCheck(player);
			} else {
				chat.joinChat(player);
			}
		}
		
	}
	
	public String getOwnerName() {
		return owner;
	}
	
	private void joinChat(Player player) {
		synchronized (this) {
			if (!player.getUsername().equals(owner) && !settings.hasRankToJoin(player.getUsername())) {
				player.getPackets().sendGameMessage("You do not have a enough rank to join this friends chat channel.");
				return;
			}
			if (players.size() >= 100) {
				player.getPackets().sendGameMessage("This chat is full.");
				return;
			}
			Long bannedSince = bannedPlayers.get(player.getUsername());
			if (bannedSince != null) {
				if (bannedSince + 3600000 > Misc.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You have been banned from this channel.");
					return;
				}
				bannedPlayers.remove(player.getUsername());
			}
			joinChatNoCheck(player);
		}
	}
	
	public void leaveChat(Player player, boolean logout) {
		synchronized (this) {
			player.setCurrentFriendChat(null);
			players.remove(player);
			if (players.size() == 0) {
				synchronized (cachedFriendChats) {
					cachedFriendChats.remove(owner);
				}
			} else {
				refreshChannel();
			}
			if (!logout) {
				player.getAttributes().setCurrentFriendChatOwner(null);
				player.getPackets().sendGameMessage("You have left the channel.");
				player.getPackets().sendFriendsChatChannel();
			}
		}
	}
	
	private void joinChatNoCheck(Player player) {
		synchronized (this) {
			players.add(player);
			player.setCurrentFriendChat(this);
			player.getAttributes().setCurrentFriendChatOwner(owner);
			player.getPackets().sendGameMessage("You are now talking in the friends chat channel " + settings.getChatName());
			player.getPackets().sendGameMessage("To talk, start each line of chat with the / symbol.");
			refreshChannel();
		}
	}
	
	public byte[] getDataBlock() {
		return dataBlock;
	}
	
	public void kickPlayerFromFriendsChannel(String name, Player player) {
		kickPlayerFromChat(player, name);
	}
	
	public void kickPlayerFromChat(Player player, String username) {
		StringBuilder name = new StringBuilder();
		for (char character : username.toCharArray()) {
			name.append(Misc.containsInvalidCharacter(character) ? " " : character);
		}
		synchronized (this) {
			int rank = getRank(player.getUsername());
			if (rank < getWhoCanKickOnChat()) {
				return;
			}
			Player kicked = getPlayerByDisplayName(name.toString());
			if (kicked == null) {
				player.getPackets().sendGameMessage("This player is not this channel.");
				return;
			}
			if (rank <= getRank(kicked.getUsername())) {
				return;
			}
			kicked.setCurrentFriendChat(null);
			kicked.getAttributes().setCurrentFriendChatOwner(null);
			players.remove(kicked);
			bannedPlayers.put(kicked.getUsername(), Misc.currentTimeMillis());
			kicked.getPackets().sendFriendsChatChannel();
			kicked.getPackets().sendGameMessage("You have been kicked from the friends chat channel.");
			player.getPackets().sendGameMessage("You have kicked " + kicked.getUsername() + " from friends chat channel.");
			refreshChannel();
		}
	}
	
	public Player getPlayerByDisplayName(String username) {
		String formatedUsername = Misc.formatPlayerNameForProtocol(username);
		for (Player player : players) {
			if (player.getUsername().equals(formatedUsername) || player.getDisplayName().equals(username)) {
				return player;
			}
		}
		return null;
	}
	
	public void sendFriendsChannelMessage(ChatMessage message, Player player) {
		sendMessage(player, message);
	}
	
	private void sendMessage(Player player, ChatMessage message) {
		synchronized (this) {
			if (!player.getUsername().equals(owner) && !settings.canTalk(player)) {
				player.getPackets().sendGameMessage("You do not have a enough rank to talk on this friends chat channel.");
				return;
			}
			String formattedName = Misc.formatPlayerNameForDisplay(player.getUsername());
			String displayName = player.getDisplayName();
			int rights = player.getMessageIcon();
			for (Player p2 : players) {
				p2.getPackets().receiveFriendChatMessage(formattedName, displayName, rights, settings.getChatName(), message);
			}
		}
	}
	
	public void sendFriendsChannelQuickMessage(QuickChatMessage message, Player player) {
		sendQuickMessage(player, message);
	}
	
	public void sendQuickMessage(Player player, QuickChatMessage message) {
		synchronized (this) {
			if (!player.getUsername().equals(owner) && !settings.canTalk(player)) {
				player.getPackets().sendGameMessage("You do not have a enough rank to talk on this friends chat channel.");
				return;
			}
			String formatedName = Misc.formatPlayerNameForDisplay(player.getUsername());
			String displayName = player.getDisplayName();
			int rights = player.getMessageIcon();
			for (Player p2 : players) {
				p2.getPackets().receiveFriendChatQuickMessage(formatedName, displayName, rights, settings.getChatName(), message);
			}
		}
	}
}
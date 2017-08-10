package org.redrune.game.node.entity.player.link;

import lombok.Getter;
import lombok.Setter;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.client.MasterCommunication;
import org.redrune.network.master.client.packet.out.FriendRequestPacketOut;
import org.redrune.network.world.packet.outgoing.impl.FriendsListBuilder;
import org.redrune.network.world.packet.outgoing.impl.IgnoreListBuilder;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.constant.GameBarStatus;
import org.redrune.utility.tool.Misc;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public class ContactManager {
	
	/**
	 * The list of friends a player has
	 */
	@Getter
	private final Set<String> friendList = new LinkedHashSet<>();
	
	/**
	 * The list of usernames a player has on their ignore list
	 */
	@Getter
	private final Set<String> ignoreList = new LinkedHashSet<>();
	
	/**
	 * The map of ranks for the clan
	 */
	private final Map<String, Integer> clanRankMap = new HashMap<>();
	
	/**
	 * The player
	 */
	@Setter
	private transient Player player;
	
	/**
	 * If we have unlocked the friends list yet. We must unlock it each time the player loads up.
	 */
	private transient boolean unlocked = false;
	
	/**
	 * Handles the friend chat management when a user logs in
	 */
	public void sendLogin() {
		// unlocks the friends list
		if (friendList.isEmpty()) {
			player.getTransmitter().send(new FriendsListBuilder().build(player));
			unlocked = true;
		} else {
			requestAllFriendsDetails();
		}
		// send all the users on our ignore list
		updateIgnoreList();
		showMyFriendsStatus(true);
	}
	
	/**
	 * Requests details for all the friends in our list
	 */
	private void requestAllFriendsDetails() {
		// requests friends details from the login server
		friendList.forEach(this::requestFriendDetails);
	}
	
	/**
	 * Updates the ignore list
	 */
	private void updateIgnoreList() {
		player.getTransmitter().send(new IgnoreListBuilder(ignoreList, getDisplayNameList()).build(player));
	}
	
	/**
	 * Shows the status of all my friends onto my friends list
	 *
	 * @param online
	 * 		If the player is online
	 */
	public void showMyFriendsStatus(boolean online) {
		final String username = player.getDetails().getUsername();
		SystemManager.getScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void run() {
				byte privateStatus = getPrivateStatus();
				byte world = player.getWorld();
				System.out.println("sent status update: [" + username + ", " + online + ", " + privateStatus + ", " + world + "]");
				//MasterCommunication.write(new StatusUpdatePacketOut(username, online, privateStatus, world));
			}
		});
	}
	
	/**
	 * Gets the list of all the players, with their display names matching
	 */
	private Map<String, String> getDisplayNameList() {
		return new HashMap<>();
	}
	
	/**
	 * Gets the current status of the player
	 */
	public byte getPrivateStatus() {
		Object barStatus = player.getVariables().getAttribute(AttributeKey.PRIVATE, GameBarStatus.ON);
		GameBarStatus status = GameBarStatus.ON;
		if (barStatus != null) {
			if (barStatus.getClass().equals(String.class)) {
				status = GameBarStatus.valueOf(barStatus.toString());
			} else {
				status = (GameBarStatus) barStatus;
			}
		}
		return status.getValue();
	}
	
	/**
	 * Handles the addition of a name to our friend list
	 *
	 * @param name
	 * 		The name of our friend
	 */
	public void addFriend(String name) {
		for (char c : name.toCharArray()) {
			if (!Misc.allowed(c)) {
				return;
			}
		}
		if (friendList.size() >= 200) {
			player.getTransmitter().sendMessage("Your friends list is full.", false);
			return;
		}
		if (hasFriend(name)) {
			player.getTransmitter().sendMessage("This player is already on your friends list.");
			return;
		}
		friendList.add(name);
		requestFriendDetails(name);
	}
	
	/**
	 * Checks if we have a friend by the name
	 *
	 * @param name
	 * 		The name of the friend.
	 */
	public boolean hasFriend(String name) {
		return friendList.contains(name);
	}
	
	/**
	 * Requests details of a friend
	 *
	 * @param requested
	 * 		The contact
	 */
	private void requestFriendDetails(String requested) {
		MasterCommunication.write(new FriendRequestPacketOut(player.getDetails().getUsername(), player.getWorld(), requested));
		System.err.println("requested details of " + requested);
	}
	
	/**
	 * Updates details for a friend
	 *
	 * @param username
	 * 		The username of the friend
	 * @param worldId
	 * 		The world id of the friend
	 * @param online
	 * 		The if the friend was online
	 */
	public void updateFriend(String username, byte worldId, boolean online) {
		// this removes the 'waiting for reply from friends server'
		// must be sent before anything else
		if (!unlocked) {
			System.err.println("Unlocked friends list!");
			player.getTransmitter().send(new FriendsListBuilder().build(player));
			unlocked = true;
		}
		
		// the rank of the user
		final int clanRank = getClanRank(username);
		// if the received world is a lobby
		boolean lobby = worldId == MasterConstants.LOBBY_WORLD_ID;
		
		player.getTransmitter().send(new FriendsListBuilder(username, "", worldId, clanRank, true, lobby, online).build(player));
	}
	
	/**
	 * Gets the rank of a contact in our settings
	 *
	 * @param username
	 * 		The name of the contact
	 */
	public int getClanRank(String username) {
		// TODO modifying ranks in the interface
		return 0;
	}
	
	/**
	 * Sends a private message
	 *
	 * @param name
	 * 		The name of the person we want to send a message to
	 * @param message
	 * 		The message we want to send
	 */
	public void sendPrivateMessage(String name, String message) {
		// TODO
		//		System.out.println("ContactManager.sendPrivateMessage");
		//		MasterCommunication.write(new PrivateMessageAttemptPacketOut(player.getDetails().getUsername(), (byte) player.getDetails().getDominantRight().getClientRight(), name, message));
	}
	
	/**
	 * Handles the addition of a username to our ignore list
	 *
	 * @param name
	 * 		The name
	 */
	public void addIgnore(String name) {
		for (char c : name.toCharArray()) {
			if (!Misc.allowed(c)) {
				return;
			}
		}
		if (ignoreList.size() >= 200) {
			player.getTransmitter().sendMessage("Your ignore list is full.", false);
			return;
		}
		if (ignoreList.contains(name)) {
			player.getTransmitter().sendMessage("This player is already on your ignore list.");
			return;
		}
		ignoreList.add(name);
		updateIgnoreList();
	}
	
	/**
	 * Handles the removal of a friends name
	 *
	 * @param name
	 * 		The name
	 */
	public void removeFriend(String name) {
		friendList.remove(name);
	}
	
	/**
	 * Handles the removal of a name from the ignore list
	 *
	 * @param name
	 * 		The name
	 */
	public void removeIgnore(String name) {
		ignoreList.remove(name);
	}
}

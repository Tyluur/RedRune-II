package org.redrune.game.node.entity.player.link.chat;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.outgoing.impl.FriendsListBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.IgnoreListBuilder;
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
	 * Handles the friend chat management when a user logs in
	 */
	public void sendLogin() {
		// unlocks the friends list
		if (friendList.isEmpty()) {
			player.getTransmitter().send(new FriendsListBuilder().build(player));
		} else {
			updateFriendList();
		}
		// send all the users on our ignore list
		updateIgnoreList();
		showMyFriendsStatus();
	}
	
	/**
	 * Updates all of our friend list data. This will keep adding more and more users onto the friend list, we cannot
	 * clear it.
	 */
	public void updateFriendList() {
		// requests friends details from the login server
		friendList.forEach(this::requestFriendDetails);
	}
	
	/**
	 * Updates the ignore list
	 */
	public void updateIgnoreList() {
		player.getTransmitter().send(new IgnoreListBuilder(ignoreList, getDisplayNameList()).build(player));
	}
	
	/**
	 * Shows the status of all my friends onto my friends list
	 */
	public void showMyFriendsStatus() {

	}
	
	/**
	 * Gets the list of all the players, with their display names matching
	 */
	private Map<String, String> getDisplayNameList() {
		return new HashMap<>();
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
		if (friendList.contains(name)) {
			player.getTransmitter().sendMessage("This player is already on your friends list.");
			return;
		}
		friendList.add(name);
		requestFriendDetails(name);
	}
	
	/**
	 * Sends the packet to the master server, requesting the friend's details
	 *
	 * @param name
	 * 		The name of the friend
	 */
	private void requestFriendDetails(String name) {
		//RS2MasterCommunication.writeMasterPacket(new ClientFriendRequestBuilder(new ClientFriendRequestContext(player.getNetworkSession().getUid(), name)).build());
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
	
	/**
	 * Checks if we have a friend by the name
	 *
	 * @param name
	 * 		The name of the friend.
	 */
	public boolean hasFriend(String name) {
		for (String friend : friendList) {
			if (friend.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
}

package org.redrune.game.node.entity.player.link.contact;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
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
	private final Set<Contact> friendList = new LinkedHashSet<>();
	
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
			System.out.println("Sent empty friends list");
			unlocked = true;
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
		// TODO:
		System.out.println("ContactManager.showMyFriendsStatus");
	/*	SystemManager.getScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void run() {
				MasterCommunication.write(new StatusUpdatePacketOut(player.getDetails().getUsername(), getStatus()));
			}
		});*/
	}
	
	/**
	 * Sends our logout status
	 */
	public void sendFriendsLogoutStatus() {
		// TODO:
		System.out.println("ContactManager.sendFriendsLogoutStatus");
		//MasterCommunication.write(new StatusUpdatePacketOut(player.getDetails().getUsername(), MasterConstants.OFFLINE_STATUS));
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
		if (hasFriend(name)) {
			player.getTransmitter().sendMessage("This player is already on your friends list.");
			return;
		}
		Contact contact = new Contact(name);
		friendList.add(contact);
		requestFriendDetails(contact);
	}
	
	/**
	 * Adds the contact to the player's client friend list
	 *
	 * @param contact
	 * 		The contact
	 */
	private void requestFriendDetails(Contact contact) {
		//MasterCommunication.write(new ContactDetailsRequestPacketOut(player.getNetworkSession().getUid(), player.getDetails().getUsername(), contact.getUsername()));
	}
	
	/**
	 * Updates details for a contact
	 *
	 * @param username
	 * 		The username of the contact
	 * @param worldId
	 * 		The world id of the contact
	 * @param status
	 * 		The status of the contact
	 */
	public void updateContact(String username, byte worldId, byte status) {
		// this removes the 'waiting for reply from friends server'
		// must be sent before anything else
/*		if (!unlocked) {
			System.err.println("Unlocked friends list!");
			player.getTransmitter().send(new FriendsListBuilder().build(player));
			unlocked = true;
		}
		
		final boolean online = status != MasterConstants.OFFLINE_STATUS;
		final int clanRank = getClanRank(username);
		
		if (worldId == GameConstants.LOBBY_WORLD_ID) {
			player.getTransmitter().send(new FriendsListBuilder(username, "", worldId, clanRank, true, true, online).build(player));
		} else {
			player.getTransmitter().send(new FriendsListBuilder(username, "", worldId, clanRank, true, false, online).build(player));
		}*/
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
		System.out.println("ContactManager.sendPrivateMessage");
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
		friendList.removeIf(contact -> contact.getUsername().equals(name));
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
		for (Contact contact : friendList) {
			if (contact.getUsername().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the current status of the player
	 */
	public byte getStatus() {
		Object barStatus = player.getVariables().getAttribute(AttributeKey.PRIVATE, GameBarStatus.ON);
		GameBarStatus status = GameBarStatus.ON;
		if (barStatus != null) {
			if (barStatus.getClass().equals(String.class)) {
				status = GameBarStatus.valueOf(barStatus.toString());
			} else {
				status = (GameBarStatus) barStatus;
			}
		}
		System.out.println("The status we have for " + player + " is " + status + " [" + status.getValue() + "]");
		return status.getValue();
	}
}

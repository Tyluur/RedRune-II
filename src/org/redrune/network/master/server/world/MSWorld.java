package org.redrune.network.master.server.world;

import lombok.Getter;
import lombok.Setter;
import org.redrune.network.master.server.MSPlayer;
import org.redrune.network.master.server.network.MSSession;

import java.util.*;

/**
 * Holds information of a certain game world.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @author Emperor
 * @since 7/12/2017
 */
public class MSWorld {
	
	/**
	 * The world id.
	 */
	@Getter
	private final byte id;
	
	/**
	 * The list of players in the world
	 */
	@Getter
	private final Set<MSPlayer> worldPlayers = new LinkedHashSet<>();
	
	/**
	 * The session
	 */
	@Getter
	@Setter
	private MSSession session;
	
	public MSWorld(byte id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "MSWorld{" + "id=" + id + ", worldPlayers=" + worldPlayers + '}';
	}
	
	/**
	 * Checks if a player is on the world
	 *
	 * @param username
	 * 		The name of the player
	 */
	public boolean isOnline(String username) {
		for (MSPlayer worldPlayerName : worldPlayers) {
			if (worldPlayerName.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds a player to the list of players
	 *
	 * @param username
	 * 		The name of the player
	 * @param uid
	 * 		The unique identification of the player
	 */
	public boolean addPlayer(String username, String uid) {
		System.out.println("MSWorld.addPlayer");
		return worldPlayers.add(new MSPlayer(username, uid, id));
	}
	
	/**
	 * Removes a player from the list of players
	 *
	 * @param username
	 * 		The name of the player
	 * @return <tt>true</tt> if the list contained the username
	 */
	public boolean removePlayer(String username) {
		System.out.println("MSWorld.removePlayer");
		System.out.println(this);
		System.out.println("username = [" + username + "]");
		return worldPlayers.removeIf(player -> player.getUsername().equals(username));
	}
	
	/**
	 * Checks if a player is registered in this world
	 *
	 * @param username
	 * 		The name of the player
	 */
	public boolean playerRegistered(String username) {
		for (MSPlayer worldPlayer : worldPlayers) {
			if (worldPlayer.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Finds a player in the world and constructs an optional for that player by their name
	 *
	 * @param username
	 * 		The name of the player in the world
	 */
	public Optional<MSPlayer> getPlayerByName(String username) {
		for (MSPlayer worldPlayer : worldPlayers) {
			if (Objects.equals(worldPlayer.getUsername(), username)) {
				return Optional.of(worldPlayer);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Finds an optional instance by the uid
	 *
	 * @param uid
	 * 		The uid of the player
	 */
	public Optional<MSPlayer> getPlayerByUid(String uid) {
		for (MSPlayer worldPlayer : worldPlayers) {
			if (Objects.equals(worldPlayer.getUid(), uid)) {
				return Optional.of(worldPlayer);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Removes the world
	 */
	void unregister() {
		worldPlayers.clear();
	}
}

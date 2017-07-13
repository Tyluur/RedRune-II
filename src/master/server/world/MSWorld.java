package master.server.world;

import lombok.Getter;
import lombok.Setter;
import master.server.network.MSSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	private final int id;
	
	/**
	 * The list of players in the lobby
	 */
	@Getter
	private final List<String> lobbyPlayers = new ArrayList<>();
	
	/**
	 * The list of players in the world
	 */
	@Getter
	private final List<String> worldPlayers = new ArrayList<>();
	
	/**
	 * The session
	 */
	@Getter
	@Setter
	private MSSession session;
	
	public MSWorld(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "MSWorld{" + "id=" + id + ", lobbyPlayers=" + lobbyPlayers + ", worldPlayers=" + worldPlayers + '}';
	}
	
	/**
	 * Checks if a player is on the world
	 *
	 * @param username
	 * 		The name of the player
	 * @param checkLobby
	 * 		If we should check the list of players in the lobby as well
	 */
	public boolean isOnline(String username, boolean checkLobby) {
		if (checkLobby) {
			for (String lobbyPlayerName : lobbyPlayers) {
				if (lobbyPlayerName.equals(username)) {
					return true;
				}
			}
		}
		for (String worldPlayerName : worldPlayers) {
			if (worldPlayerName.equals(username)) {
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
	 * @param lobby
	 * 		If the player should go to the lobby list or the world list
	 * @return <tt>true</tt> (as specified by {@link Collection#add})
	 */
	public boolean addPlayer(String username, boolean lobby) {
		return (lobby ? lobbyPlayers : worldPlayers).add(username);
	}
	
	/**
	 * Removes a player from the list of players
	 *
	 * @param username
	 * 		The name of the player
	 * @param lobby
	 * 		If we should remove the player from the lobby list or the world list
	 * @return <tt>true</tt> if the list contained the username
	 */
	public boolean removePlayer(String username, boolean lobby) {
		return (lobby ? lobbyPlayers : worldPlayers).remove(username);
	}
	
	/**
	 * Checks if a player is registered in this world
	 *
	 * @param username
	 * 		The name of the player
	 */
	public boolean playerRegistered(String username) {
		for (String lobbyPlayer : lobbyPlayers) {
			if (lobbyPlayer.equals(username)) {
				return true;
			}
		}
		for (String worldPlayer : worldPlayers) {
			if (worldPlayer.equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes the world
	 */
	void unregister() {
		lobbyPlayers.clear();
		worldPlayers.clear();
	}
}

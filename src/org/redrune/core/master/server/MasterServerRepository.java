package org.redrune.core.master.server;

import lombok.Getter;
import org.redrune.core.master.MasterPlayer;
import org.redrune.utility.Misc;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/9/2017
 */
public class MasterServerRepository {
	
	/**
	 * The list of players that are in the lobby
	 */
	private final CopyOnWriteArraySet<MasterPlayer> lobbyPlayers;
	
	/**
	 * The list of worlds
	 */
	private final Map<Integer, MasterWorld> worldMap;
	
	/**
	 * The update workr
	 */
	@Getter
	private final MasterUpdateWorker updateWorker;
	
	/**
	 * The logger
	 */
	private final Logger logger = Misc.constructLogger(MasterServerRepository.class);
	
	public MasterServerRepository() {
		this.lobbyPlayers = new CopyOnWriteArraySet<>();
		this.worldMap = new ConcurrentHashMap<>();
		this.updateWorker = new MasterUpdateWorker(this);
		
		// adding worlds
		addWorld(1);
		addWorld(2);
	}
	
	/**
	 * Adds the world
	 *
	 * @param worldId
	 * 		The id of the world to add
	 */
	private MasterWorld addWorld(int worldId) {
		return this.worldMap.put(worldId, new MasterWorld(worldId));
	}
	
	/**
	 * The username
	 */
	public boolean userLoggedIn(String username, boolean checkLobby) {
		if (checkLobby) {
			Optional<MasterPlayer> optional = lobbyPlayers.stream().filter(player -> player.getUsername().equalsIgnoreCase(username)).findFirst();
			// the user is in the lobby
			if (optional.isPresent()) {
				return true;
			}
		}
		System.out.println(worldMap.entrySet());
		// if the player is in a world
		for (Entry<Integer, MasterWorld> entry : worldMap.entrySet()) {
			if (entry.getValue().getPlayers().stream().anyMatch(player -> player.getUsername().equalsIgnoreCase(username))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds a player to the list of lobby players
	 *
	 * @param uid
	 * 		The uid of the player's session
	 * @param username
	 * 		The username of the player
	 */
	public boolean addLobbyPlayer(long uid, String username) {
		return lobbyPlayers.add(new MasterPlayer(uid, username));
	}
	
	/**
	 * Adds a player to a world
	 *
	 * @param uid
	 * 		The uid of the player's session
	 * @param username
	 * 		The username of the player
	 * @param worldId
	 * 		The id of the world
	 */
	public boolean addWorldPlayer(long uid, String username, int worldId) {
		if (!worldMap.containsKey(worldId)) {
			logger.severe("Unable to find world #" + worldId);
			return false;
		}
		MasterWorld world = worldMap.get(worldId);
		return world.getPlayers().add(new MasterPlayer(uid, username));
	}
	
	/**
	 * Removes a player from the world
	 *
	 * @param username
	 * 		The username of the player
	 */
	public void removeWorldPlayer(String username) {
		for (Entry<Integer, MasterWorld> entry : worldMap.entrySet()) {
			final MasterWorld world = entry.getValue();
			world.removePlayer(username);
		}
	}
	
	/**
	 * Removes a player from the lobby
	 *
	 * @param username
	 * 		The username of the player
	 */
	public void removeLobbyPlayer(String username) {
		lobbyPlayers.removeIf(player -> player.getUsername().equalsIgnoreCase(username));
	}
	
	/**
	 * Gets the amount of players in a world
	 *
	 * @param worldId
	 * 		The id of the world
	 */
	public int getPlayerCount(int worldId) {
		if (!isWorldOnline(worldId)) {
			return 0;
		}
		if (!worldMap.containsKey(worldId)) {
			return 0;
		}
		MasterWorld world = worldMap.get(worldId);
		return world.getPlayers().size();
	}
	
	/**
	 * Checks if a world is online
	 *
	 * @param worldId
	 * 		The id of the world
	 */
	public boolean isWorldOnline(int worldId) {
		return worldMap.containsKey(worldId) && worldMap.get(worldId).isOnline();
	}
	
	/**
	 * Sets the worlds online flag
	 *
	 * @param worldId
	 * 		The id of the world
	 * @param online
	 * 		If it is online
	 */
	public void setWorldOnline(int worldId, boolean online) {
		MasterWorld world = worldMap.get(worldId);
		if (world == null) {
			return;
		}
		world.setOnline(online);
	}
	
	/**
	 * Get the amount of worlds we have
	 */
	public int getWorldCount() {
		return worldMap.size();
	}
	
	/**
	 * Gets a world by its id
	 *
	 * @param worldId
	 * 		The world
	 */
	public MasterWorld getWorld(int worldId) {
		return worldMap.get(worldId);
	}
}
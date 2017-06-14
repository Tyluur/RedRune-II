package org.redrune.core.master.server;

import lombok.Getter;
import org.jboss.netty.channel.Channel;
import org.redrune.core.master.MasterPlayer;
import org.redrune.network.master.MasterPacket;
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
	 * The update work
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
	}
	
	/**
	 * Checks if a world is verified
	 *
	 * @param worldId
	 * 		The id of the world
	 */
	public boolean isWorldVerified(int worldId) {
		return worldMap.containsKey(worldId);
	}
	
	/**
	 * Adds the world
	 *
	 * @param worldId
	 * 		The id of the world to add
	 * @param channel
	 * 		The channel of the world
	 */
	public void addWorld(int worldId, Channel channel) {
		final MasterWorld world = new MasterWorld(worldId);
		world.setChannel(channel);
		worldMap.put(worldId, world);
		setWorldOnline(worldId, isWorldOnline(worldId));
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
	 * 		The players name
	 * @param worldId
	 * 		The id of the world the player is in
	 */
	public boolean addLobbyPlayer(long uid, String username, int worldId) {
		return lobbyPlayers.add(new MasterPlayer(uid, username, worldId));
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
		return world.getPlayers().add(new MasterPlayer(uid, username, worldId));
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
	
	/**
	 * Gets the world the player is on
	 *
	 * @param username
	 * 		The name of the player
	 * @return 0 if lobby, -1 if offline.
	 */
	public int getWorldId(String username) {
		Optional<MasterPlayer> optional = lobbyPlayers.stream().filter(player -> player.getUsername().equalsIgnoreCase(username)).findFirst();
		// the user is in the lobby
		if (optional.isPresent()) {
			return 0;
		}
		// if the player is in a world
		for (Entry<Integer, MasterWorld> entry : worldMap.entrySet()) {
			if (entry.getValue().getPlayers().stream().anyMatch(player -> player.getUsername().equalsIgnoreCase(username))) {
				return entry.getKey();
			}
		}
		return -1;
	}
	
	/**
	 * Writes a packet to the world
	 *
	 * @param worldId
	 * 		The world
	 * @param packet
	 * 		The packet
	 */
	public boolean writeToWorld(int worldId, MasterPacket packet) {
		try {
			MasterWorld world = worldMap.get(worldId);
			if (world == null) {
				logger.severe("Unable to write packet to world " + worldId);
				System.out.println(lobbyPlayers);
				System.out.println(worldMap);
				return false;
			}
			Channel channel = world.getChannel();
			if (channel == null || !channel.isWritable() || !channel.isConnected()) {
				logger.severe("World #" + worldId + " has invalid channel!");
				return false;
			}
			channel.write(packet);
			System.out.println("Writing packet to world # " + worldId + " [" + packet.getOpcode() + "]");
			System.out.println("Channel info: " + Misc.printChannel(channel));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Writes a packet to the users world
	 *
	 * @param username
	 * 		The username of the user
	 * @param packet
	 * 		The player
	 */
	public boolean writeToUser(String username, MasterPacket packet) {
		int worldId = -1;
		for (MasterPlayer player : lobbyPlayers) {
			if (player.getUsername().equals(username)) {
				worldId = player.getWorldId();
				break;
			}
		}
		for (MasterWorld world : worldMap.values()) {
			for (MasterPlayer player : world.getPlayers()) {
				if (player.getUsername().equals(username)) {
					worldId = player.getWorldId();
					break;
				}
			}
		}
		if (worldId == -1) {
			logger.severe("Unable to find world of '" + username + "'.");
			logger.info(worldMap.toString());
			logger.info(lobbyPlayers.toString());
			return false;
		}
		writeToWorld(worldId, packet);
		return true;
	}
	
	/**
	 * Writes a packet to all the active worlds
	 *
	 * @param packet
	 * 		The packet to write
	 */
	public boolean writeToAllWorlds(MasterPacket packet) {
		for (Integer worldId : worldMap.keySet()) {
			if (!writeToWorld(worldId, packet)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets the uid of a user
	 *
	 * @param username
	 * 		The username of the user
	 */
	public long getUid(String username) {
		for (MasterPlayer player : lobbyPlayers) {
			if (player.getUsername().equals(username)) {
				return player.getUid();
			}
		}
		for (MasterWorld world : worldMap.values()) {
			for (MasterPlayer player : world.getPlayers()) {
				if (player.getUsername().equals(username)) {
					return player.getUid();
				}
			}
		}
		return -1;
	}
}
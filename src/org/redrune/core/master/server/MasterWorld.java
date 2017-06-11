package org.redrune.core.master.server;

import lombok.Getter;
import lombok.Setter;
import org.redrune.core.master.MasterPlayer;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public final class MasterWorld {
	
	/**
	 * The id of the world
	 */
	@Getter
	private final int worldId;
	
	/**
	 * The set of players that are logged into the world
	 */
	@Getter
	private final CopyOnWriteArraySet<MasterPlayer> players;
	
	/**
	 * If the world is online
	 */
	@Getter
	@Setter
	private boolean isOnline = false;
	
	public MasterWorld(int worldId) {
		this.worldId = worldId;
		this.players = new CopyOnWriteArraySet<>();
	}
	
	@Override
	public String toString() {
		return "MasterWorld{" + "worldId=" + worldId + ", players=" + players + '}';
	}
	
	/**
	 * Removes a player from the {@link #players} list
	 *
	 * @param username
	 * 		The username of the player
	 */
	public void removePlayer(String username) {
		players.removeIf(player -> player.getUsername().equalsIgnoreCase(username));
	}
}

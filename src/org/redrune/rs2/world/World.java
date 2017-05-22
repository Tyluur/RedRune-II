package org.redrune.rs2.world;

import lombok.Getter;
import lombok.Setter;
import org.redrune.rs2.GameConstants;
import org.redrune.rs2.node.entity.EntityList;
import org.redrune.rs2.node.entity.player.Player;

import java.util.Optional;

/**
 * Contains all the collections and data to handle a world.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class World {
	
	/**
	 * The world singleton
	 */
	private static World singleton = null;
	
	/**
	 * The list of all players in the world
	 */
	@Getter
	private final EntityList<Player> players = new EntityList<>(GameConstants.PLAYERS_LIMIT, true);
	
	/**
	 * If the world is alive
	 */
	@Getter
	@Setter
	private boolean isAlive;
	
	public World() {
		setAlive(true);
	}
	
	/** The {@code EntityList} of all npcs that exist. */
	//private static final EntityList<NPC> npcs = new EntityList<>(GameConstants.NPCS_LIMIT, false);
	
	/**
	 * Gets the singleton instance
	 *
	 * @return A {@code World} {@code Object}
	 */
	public static World get() {
		if (singleton == null) {
			singleton = new World();
		}
		return singleton;
	}
	
	/**
	 * Finds a player by their username
	 *
	 * @param username
	 * 		The username of the player
	 */
	public Optional<Player> getPlayerByUsername(String username) {
		return players.stream().filter(player -> player.getDetails().getUsername().equals(username)).findAny();
	}
}

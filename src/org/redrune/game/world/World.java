package org.redrune.game.world;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.GameConstants;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.EntityList;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.repository.npc.spawn.NPCSpawn;
import org.redrune.utility.rs.constant.Directions.Direction;

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
	 * The list of all players in the lobby
	 */
	@Getter
	private final EntityList<Player> lobbyPlayers = new EntityList<>(GameConstants.PLAYERS_LIMIT, true);
	
	/**
	 * The {@code EntityList} of all npcs that exist.
	 */
	@Getter
	private final EntityList<NPC> npcs = new EntityList<>(GameConstants.NPCS_LIMIT, false);
	
	/**
	 * If the world is alive
	 */
	@Getter
	@Setter
	private boolean isAlive;
	
	/**
	 * Constructs a new world object
	 */
	private World() {
		setAlive(true);
	}
	
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
	 * Adds an npc to the world, in the form of a {@link NPCSpawn} {@code Object}
	 *
	 * @param spawn
	 * 		The {@code NPCSpawn} object
	 */
	public World addSpawn(NPCSpawn spawn) {
		addNPC(spawn.getNpcId(), spawn.getTile(), spawn.getDirection());
		return this;
	}
	
	/**
	 * Adds an npc to the world
	 *
	 * @param id
	 * 		The id of the npc
	 * @param location
	 * 		The location of the npc
	 */
	public NPC addNPC(int id, Location location, Direction direction) {
		final NPC npc = new NPC(id, location, direction);
		npc.register();
		npcs.add(npc);
		return npc;
	}
	
	/**
	 * Handles the removal of a player
	 *
	 * @param player
	 * 		The player to remove
	 * @param lobby
	 */
	public void removePlayer(Player player, boolean lobby) {
		if (!lobby) {
			players.remove(player);
		} else {
			lobbyPlayers.remove(player);
		}
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

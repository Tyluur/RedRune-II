package org.redrune.rs2.world;

import lombok.Getter;
import lombok.Setter;
import org.redrune.rs2.GameConstants;
import org.redrune.rs2.node.entity.EntityList;
import org.redrune.rs2.node.entity.npc.NPC;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.world.map.Location;
import org.redrune.rs2.world.task.Scheduler;

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
	 * The {@code EntityList} of all npcs that exist.
	 */
	@Getter
	private final EntityList<NPC> npcs = new EntityList<>(GameConstants.NPCS_LIMIT, false);
	
	/**
	 * The task scheduler.
	 */
	@Getter
	private final Scheduler scheduler;
	
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
		this.scheduler = new Scheduler();
		addNPC(50, Location.create(3333, 3333, 0));
		setAlive(true);
	}
	
	/**
	 * Adds an npc to the world
	 *
	 * @param id
	 * 		The id of the npc
	 * @param location
	 * 		The location of the npc
	 */
	public void addNPC(int id, Location location) {
		final NPC npc = new NPC(id, location);
		npc.register();
		npcs.add(npc);
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
	 * Finds a player by their username
	 *
	 * @param username
	 * 		The username of the player
	 */
	public Optional<Player> getPlayerByUsername(String username) {
		return players.stream().filter(player -> player.getDetails().getUsername().equals(username)).findAny();
	}
}

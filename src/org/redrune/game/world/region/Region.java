package org.redrune.game.world.region;

import lombok.Getter;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.repository.npc.spawn.NPCSpawnRepository;
import org.redrune.utility.rs.constant.RegionConstants;

import java.util.*;

/**
 * @author Emperor
 * @author Dementhium development team (mainly).
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class Region {
	
	/**
	 * The player synchronization lock.
	 */
	private final Object playerLock = new Object();
	
	/**
	 * The npc synchronization lock.
	 */
	private final Object npcLock = new Object();
	
	/**
	 * A list of players in this region.
	 */
	@SuppressWarnings("unchecked")
	private final SortedSet<Player> players;
	
	/**
	 * A list of NPCs in this region.
	 */
	@SuppressWarnings("unchecked")
	private final List<NPC> npcs;
	
	/**
	 * The id of the region
	 */
	@Getter
	private final int id;
	
	/**
	 * The base x-coordinate of this region.
	 */
	@Getter
	private final int x;
	
	/**
	 * The base y-coordinate of this region.
	 */
	@Getter
	private final int y;
	
	/**
	 * The clipping masks of this region.
	 */
	private final int[][][] clippingMasks = new int[4][][];
	
	/**
	 * If all the spawns have been loaded.
	 */
	private final boolean[] loadedFlags;
	
	/**
	 * A list of game objects on this region.
	 */
	@Getter
	private List<GameObject> objects;
	
	/**
	 * Constructs a new {@code Region} {@code Object}.
	 *
	 * @param id
	 * 		The id of the region
	 */
	public Region(int id) {
		this.id = id;
		this.x = (id >> 8) * 64;
		this.y = (id & 0xff) * 64;
		this.players = new TreeSet<>(Comparator.comparingInt(Entity::getIndex));
		this.npcs = new ArrayList<>();
		this.loadedFlags = new boolean[2];
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ", players=" + players + ", npcs=" + npcs + ", objects=" + objects + "]";
	}
	
	/**
	 * Adds an entity to this region.
	 *
	 * @param entity
	 * 		The entity to add.
	 */
	public void addEntity(Entity entity) {
		if (entity.isPlayer()) {
			synchronized (playerLock) {
				players.add(entity.toPlayer());
			}
		} else if (entity.isNPC()) {
			synchronized (npcLock) {
				if (!npcs.contains(entity.toNPC())) {
					npcs.add(entity.toNPC());
				}
			}
		}
		if (entity.isPlayer()) {
			loadRegionSpawns();
		}
	}
	
	/**
	 * Loads all the region's spawns
	 */
	public void loadRegionSpawns() {
		checkSpawns();
	}
	
	/**
	 * Checks for all spawns to be done
	 */
	private void checkSpawns() {
		if (loadedFlags[RegionConstants.LOADED_NPCS_FLAG]) {
			return;
		}
		NPCSpawnRepository.loadSpawns(id);
		loadedFlags[RegionConstants.LOADED_NPCS_FLAG] = true;
	}
	
	/**
	 * Removes an entity from this region.
	 *
	 * @param entity
	 * 		The entity to remove.
	 */
	public void removeEntity(Entity entity) {
		if (entity.isPlayer()) {
			synchronized (playerLock) {
				players.remove(entity.toPlayer());
			}
		} else if (entity.isNPC()) {
			synchronized (npcLock) {
				npcs.remove(entity.toNPC());
			}
		}
	}
	
	/**
	 * Sets the clipping flag.
	 *
	 * @param x
	 * 		The local x coordinate.
	 * @param y
	 * 		The local y coordinate.
	 * @param z
	 * 		The height.
	 * @param flag
	 * 		The flag to set.
	 */
	public void setClippingFlag(int x, int y, int z, int flag) {
		clippingMasks[z][x][y] = flag;
	}
	
	/**
	 * Gets the clipping masks in this region.
	 *
	 * @return The clipping masks.
	 */
	synchronized int[][][] getClippingMasks() {
		return clippingMasks;
	}
	
	/**
	 * @return the players
	 */
	public SortedSet<Player> getPlayers() {
		synchronized (playerLock) {
			return players;
		}
	}
	
	/**
	 * @return the npcs
	 */
	public List<NPC> getNpcs() {
		synchronized (npcLock) {
			return npcs;
		}
	}
	
	/**
	 * Adds a game object.
	 *
	 * @param object
	 * 		The object to add.
	 */
	public void addObject(GameObject object) {
		if (objects == null) {
			objects = new ArrayList<>();
		}
		objects.add(object);
	}
	
	/**
	 * Removes a game object.
	 *
	 * @param oldObj
	 * 		The object to remove.
	 */
	public void removeObject(GameObject oldObj) {
		if (objects != null) {
			objects.remove(oldObj);
		}
	}
	
	/**
	 * Gets a game object from the objects list.
	 *
	 * @param id
	 * 		The object id.
	 * @return The game object, or null if the list didn't contain this object.
	 */
	public GameObject getGameObject(int id) {
		if (objects == null) {
			return null;
		}
		for (GameObject object : objects) {
			if (object.getId() == id) {
				return object;
			}
		}
		return null;
	}
	
}

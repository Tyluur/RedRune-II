package org.redrune.game.world.region;

import lombok.Getter;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.impl.FloorItemTask;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.FloorItem;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.world.World;
import org.redrune.network.rs666.packet.outgoing.impl.FloorItemAdditionBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.FloorItemRemovalBuilder;
import org.redrune.utility.repository.npc.spawn.NPCSpawnRepository;
import org.redrune.utility.rs.constant.RegionConstants;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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
	 * The list of floor items
	 */
	private final CopyOnWriteArrayList<FloorItem> floorItems;
	
	/**
	 * A list of game objects on this region.
	 */
	@Getter
	private final CopyOnWriteArrayList<GameObject> objects;
	
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
		this.floorItems = new CopyOnWriteArrayList<>();
		this.players = new TreeSet<>(Comparator.comparingInt(Entity::getIndex));
		this.npcs = new ArrayList<>();
		this.objects = new CopyOnWriteArrayList<>();
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
		loadedFlags[RegionConstants.LOADED_NPCS_FLAG] = true;
		NPCSpawnRepository.loadSpawns(id);
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
	 * @param x
	 * 		The x coordinate of the object
	 * @param y
	 * 		The y coordinate of the object
	 * @param plane
	 * 		The plane of the object
	 * @return The game object, or null if the list didn't contain this object.
	 */
	public Optional<GameObject> findGameObject(int id, int x, int y, int plane) {
		return objects.stream().filter(object -> object.getId() == id && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane).findFirst();
	}
	
	/**
	 * Gets an optional {@code FloorItem} for the item id we're looking for
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param x
	 * 		The x coordinate of the item
	 * @param y
	 * 		The y coordinate of the item
	 * @param plane
	 * 		The plane the player is on
	 */
	public Optional<FloorItem> getFloorItem(int itemId, int x, int y, int plane) {
		return floorItems.stream().filter(item -> item.isRenderable() && item.getId() == itemId && item.getLocation().getX() == x && item.getLocation().getY() == y && item.getLocation().getPlane() == plane).findFirst();
	}
	
	/**
	 * Adds the floor item to the list
	 *
	 * @param item
	 * 		The item
	 */
	boolean addFloorItemToList(FloorItem item) {
		return floorItems.add(item);
	}
	
	/**
	 * Handles the addition of a new item to the region
	 *
	 * @param item
	 * 		The item added
	 */
	void handleAddition(FloorItem item) {
		if (item.isDefaultPublic()) {
			sendFloorItemToAll(item, false);
		} else {
			Optional<Player> optional = World.get().getPlayerByUsername(item.getOwnerUsername());
			if (!optional.isPresent()) {
				return;
			}
			final Player player = optional.get();
			player.getTransmitter().send(new FloorItemAdditionBuilder(item).build(player));
		}
		SystemManager.getScheduler().schedule(new FloorItemTask(item));
	}
	
	/**
	 * Handles the player entering the region
	 *
	 * @param player
	 * 		The player
	 */
	public void handleRegionEntry(Player player) {
		for (FloorItem item : floorItems) {
			if (!item.isRenderable()) {
				continue;
			}
			player.getTransmitter().send(new FloorItemAdditionBuilder(item).build(player));
		}
	}
	
	/**
	 * Sends the floor item to everyone in the region
	 *
	 * @param item
	 * 		The item
	 * @param skipOwner
	 * 		If we should skip the owner
	 */
	public void sendFloorItemToAll(FloorItem item, boolean skipOwner) {
		players.stream().filter(player -> {
			if (player != null) {
				if (skipOwner) {
					Optional<Player> optional = World.get().getPlayerByUsername(item.getOwnerUsername());
					if (optional.isPresent() && optional.get().equals(player)) {
						return false;
					}
				}
				if (player.getLocation().getPlane() == item.getLocation().getPlane() && player.isRenderable() && player.getMapRegionsIds().contains(id)) {
					return true;
				}
				return true;
			}
			return false;
		}).forEach(player -> player.getTransmitter().send(new FloorItemAdditionBuilder(item).build(player)));
	}
	
	/**
	 * Removes a floor item from the region
	 *
	 * @param item
	 * 		The floor item
	 */
	public void removeFloorItem(FloorItem item) {
		if (!floorItems.contains(item)) {
			return;
		}
		floorItems.remove(item);
		players.stream().filter(player -> player != null && player.getLocation().getPlane() == item.getLocation().getPlane() && player.isRenderable()).forEach(player -> player.getTransmitter().send(new FloorItemRemovalBuilder(item).build(player)));
	}
}

package org.redrune.game.world.region;

import lombok.Getter;
import org.redrune.cache.parse.MapRegionParser;
import org.redrune.cache.parse.ObjectDefinitionParser;
import org.redrune.cache.parse.definition.ObjectDefinition;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.impl.FloorItemTask;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.FloorItem;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.node.object.GameObject.ObjectType;
import org.redrune.game.world.World;
import org.redrune.network.rs666.packet.outgoing.impl.FloorItemAdditionBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.FloorItemRemovalBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.ObjectAdditionBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.ObjectRemovalBuilder;
import org.redrune.utility.repository.npc.spawn.NPCSpawnRepository;
import org.redrune.utility.rs.constant.RegionConstants;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

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
	private final int regionId;
	
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
	 * A list of game defaultObjects on this region.
	 */
	@Getter
	private final CopyOnWriteArrayList<GameObject> defaultObjects;
	
	/**
	 * The list of objects that have been removed from the region.
	 */
	@Getter
	private final CopyOnWriteArraySet<GameObject> removedObjects;
	
	/**
	 * The list of objects that have been spawned in the region
	 */
	@Getter
	private final CopyOnWriteArraySet<GameObject> spawnedObjects;
	
	/**
	 * The list of objects that were deleted (these will never be spawned)
	 */
	@Getter
	private final CopyOnWriteArraySet<GameObject> deletedObjects;
	
	/**
	 * Constructs a new {@code Region} {@code Object}.
	 *
	 * @param regionId
	 * 		The id of the region
	 */
	public Region(int regionId) {
		this.regionId = regionId;
		this.x = (regionId >> 8) * 64;
		this.y = (regionId & 0xff) * 64;
		this.floorItems = new CopyOnWriteArrayList<>();
		this.players = new TreeSet<>(Comparator.comparingInt(Entity::getIndex));
		this.npcs = new ArrayList<>();
		this.loadedFlags = new boolean[2];
		
		this.defaultObjects = new CopyOnWriteArrayList<>();
		this.removedObjects = new CopyOnWriteArraySet<>();
		this.spawnedObjects = new CopyOnWriteArraySet<>();
		this.deletedObjects = RegionManager.findDeletedObjects(this);
	}
	
	@Override
	public String toString() {
		return "[id=" + regionId + ", players=" + players + ", npcs=" + npcs + "]";
	}
	
	/**
	 * Loads the landscape
	 *
	 * @param keys
	 * 		The keys
	 */
	public void loadLandscape(int[] keys) {
		if (RegionManager.BROKEN_REGIONS.contains(regionId)) {
			return;
		}
		List<GameObject> defaultObjects = MapRegionParser.parseMap(regionId, keys);
		if (defaultObjects.isEmpty()) {
			return;
		}
		defaultObjects.forEach(object -> spawnObject(object, true));
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
	private void loadRegionSpawns() {
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
		NPCSpawnRepository.loadSpawns(regionId);
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
	public synchronized int[][][] getClippingMasks() {
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
	void addDefaultObject(GameObject object) {
		object.setSpawnType(ObjectType.CACHE);
		defaultObjects.add(object);
	}
	
	/**
	 * Finds any game object (cache/spawned)
	 *
	 * @param objectId
	 * 		The id of the object, -1 to find any
	 * @param x
	 * 		The x coordinate of the object
	 * @param y
	 * 		The y coordinate of the object
	 * @param plane
	 * 		The plane of the object
	 * @param type
	 * 		The type of the object
	 * @return The game object, or null if the list didn't contain this object.
	 */
	public Optional<GameObject> findAnyGameObject(int objectId, int x, int y, int plane, int type) {
		Optional<GameObject> optional = findDefaultGameObject(objectId, x, y, plane, type);
		if (!optional.isPresent()) {
			return spawnedObjects.stream().filter(object -> {
				if (objectId == -1) {
					if (type == -1) {
						return object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
					} else {
						return object.getType() == type && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
					}
				} else {
					if (type == -1) {
						return object.getId() == objectId && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
					} else {
						return object.getId() == objectId && object.getType() == type && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
					}
				}
			}).findFirst();
		}
		return optional;
	}
	
	/**
	 * Gets a game object from the {@link #defaultObjects} list.
	 *
	 * @param objectId
	 * 		The object id. If we don't know the id we enter -1 and find the first one that matches the other flags
	 * @param x
	 * 		The x coordinate of the object
	 * @param y
	 * 		The y coordinate of the object
	 * @param plane
	 * 		The plane of the object
	 * @param type
	 * 		The type of the object
	 * @return The game object, or null if the list didn't contain this object.
	 */
	public Optional<GameObject> findDefaultGameObject(int objectId, int x, int y, int plane, int type) {
		return defaultObjects.stream().filter(object -> {
			if (objectId == -1) {
				if (type == -1) {
					return object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				} else {
					return object.getType() == type && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				}
			} else {
				if (type == -1) {
					return object.getId() == objectId && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				} else {
					return object.getId() == objectId && object.getType() == type && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				}
			}
		}).findFirst();
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
			optional.get().getTransmitter().send(new FloorItemAdditionBuilder(item).build(optional.get()));
		}
		SystemManager.getScheduler().schedule(new FloorItemTask(item));
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
				if (player.getLocation().getPlane() == item.getLocation().getPlane() && player.isRenderable() && player.getMapRegionsIds().contains(regionId)) {
					return true;
				}
				return true;
			}
			return false;
		}).forEach(player -> player.getTransmitter().send(new FloorItemAdditionBuilder(item).build(player)));
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
		refreshAllObjects(player);
	}
	
	/**
	 * Refreshes all objects in the region
	 *
	 * @param player
	 * 		The player
	 */
	public void refreshAllObjects(Player player) {
		deletedObjects.forEach(object -> player.getTransmitter().send(new ObjectRemovalBuilder(object).build(player)));
		removedObjects.forEach(object -> player.getTransmitter().send(new ObjectRemovalBuilder(object).build(player)));
		spawnedObjects.forEach(object -> player.getTransmitter().send(new ObjectAdditionBuilder(object).build(player)));
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
	
	/**
	 * Checks if the deleted list contains the object
	 *
	 * @param object
	 * 		The object
	 */
	private boolean deleteListContains(GameObject object) {
		return deletedObjects.stream().anyMatch(deleted -> deleted.getId() == object.getId() && deleted.getLocation().equals(object.getLocation()) && deleted.getType() == object.getType());
	}
	
	/**
	 * Spawns an object into the region
	 *
	 * @param object
	 * 		The object
	 * @param defaultObject
	 * 		If the object is as default game object
	 */
	public void spawnObject(GameObject object, boolean defaultObject) {
		if (defaultObject) {
			if (deleteListContains(object)) {
//				System.out.println("Unable to spawn or clip " + object);
				return;
			}
			addDefaultObject(object);
		} else {
			Optional<GameObject> spawnedOptional = findSpawnedGameObject(object.getId(), object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getPlane(), object.getType());
			if (spawnedOptional.isPresent()) {
				final GameObject spawned = spawnedOptional.get();
				spawnedObjects.remove(spawned);
				removedObjects.add(spawned);
				unclipObjectTerritory(spawned);
			}
			spawnedObjects.add(object);
			object.setSpawnType(ObjectType.SERVER);
		}
		clipObjectTerritory(object);
		if (!defaultObject) {
			players.forEach(this::refreshAllObjects);
		}
	}
	
	/**
	 * Gets a game object from the {@link #spawnedObjects} list.
	 *
	 * @param objectId
	 * 		The id of the object, -1 if we should not check
	 * @param x
	 * 		The x coordinate of the object
	 * @param y
	 * 		The y coordinate of the object
	 * @param plane
	 * 		The plane of the object
	 * @param type
	 * 		The type of the object
	 * @return The game object, or null if the list didn't contain this object.
	 */
	public Optional<GameObject> findSpawnedGameObject(int objectId, int x, int y, int plane, int type) {
		return spawnedObjects.stream().filter(object -> {
			if (objectId == -1) {
				if (type == -1) {
					return object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				} else {
					return object.getType() == type && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				}
			} else {
				if (type == -1) {
					return objectId == object.getId() && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				} else {
					return objectId == object.getId() && type == object.getType() && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				}
			}
		}).findFirst();
	}
	
	/**
	 * Unclips the territory of an object
	 *
	 * @param object
	 * 		The object
	 */
	private void unclipObjectTerritory(GameObject object) {
		ObjectDefinition def = ObjectDefinitionParser.forId(object.getId());
		int xLength;
		int yLength;
		if (object.getRotation() != 1 && object.getRotation() != 3) {
			xLength = def.getSizeX();
			yLength = def.getSizeY();
		} else {
			xLength = def.getSizeY();
			yLength = def.getSizeX();
		}
		if (object.getType() == 22) {
			if (def.getActionCount() == 1) {
				RegionBuilder.removeClipping(x, y, object.getLocation().getPlane(), 0x200000);
			}
		} else if (object.getType() >= 9 && object.getType() <= 11) {
			if (def.getActionCount() != 0) {
				RegionBuilder.removeClippingForSolidObject(x, y, object.getLocation().getPlane(), xLength, yLength, def.isSolid(), !def.isClippingFlag());
			}
		} else if (object.getType() >= 0 && object.getType() <= 3) {
			if (def.getActionCount() != 0) {
				RegionBuilder.removeClippingForVariableObject(x, y, object.getLocation().getPlane(), object.getType(), object.getRotation(), def.isSolid(), !def.isClippingFlag());
			}
		}
	}
	
	/**
	 * Clips the territory an object will be in
	 *
	 * @param object
	 * 		The object
	 */
	private void clipObjectTerritory(GameObject object) {
		ObjectDefinition def = object.getDefinitions();
		if (def == null) {
			return;
		}
		int xLength;
		int yLength;
		if (object.getRotation() == 1 || object.getRotation() == 3) {
			xLength = def.getSizeX();
			yLength = def.getSizeY();
		} else {
			xLength = def.getSizeY();
			yLength = def.getSizeX();
		}
		if (object.getType() == 22) {
			if (def.getActionCount() == 1) {
				RegionBuilder.addClipping(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getPlane(), 0x200000);
			}
		} else if (object.getType() >= 9 && object.getType() <= 11) {
			if (def.getActionCount() != 0) {
				RegionBuilder.addClippingForSolidObject(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getPlane(), xLength, yLength, def.isSolid(), !def.isClippingFlag());
			}
		} else if (object.getType() >= 0 && object.getType() <= 3) {
			if (def.getActionCount() != 0) {
				RegionBuilder.addClippingForVariableObject(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getPlane(), object.getType(), object.getRotation(), def.isSolid(), !def.isClippingFlag());
			}
		}
	}
	
	/**
	 * Deletes an object
	 *
	 * @param object
	 * 		The object
	 */
	public void deleteObject(GameObject object) {
		if (!removeDefaultObject(object)) {
			removedObjects.add(object);
		}
		
	}
	
	/**
	 * Removes a game object from the {@link #defaultObjects} list
	 *
	 * @param oldObj
	 * 		The object to remove.
	 */
	public boolean removeDefaultObject(GameObject oldObj) {
		return defaultObjects.remove(oldObj);
	}
}
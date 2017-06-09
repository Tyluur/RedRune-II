package org.redrune.game.world.region;

import lombok.Getter;
import org.redrune.cache.parse.MapRegionParser;
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

import java.util.List;
import java.util.Optional;
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
	 * A list of players in this region.
	 */
	@Getter
	private final CopyOnWriteArraySet<Player> players;
	
	/**
	 * A list of NPCs in this region.
	 */
	@Getter
	private final CopyOnWriteArraySet<NPC> npcs;
	
	/**
	 * The id of the region
	 */
	@Getter
	private final int regionId;
	
	/**
	 * The map of the region
	 */
	@Getter
	private RegionMap map;
	
	/**
	 * The clipped only map
	 */
	private RegionMap clipedOnlyMap;
	
	/**
	 * If all the spawns have been loaded.
	 */
	@Getter
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
		this.floorItems = new CopyOnWriteArrayList<>();
		this.players = new CopyOnWriteArraySet<>();
		this.npcs = new CopyOnWriteArraySet<>();
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
			System.out.println("Broken region #" + regionId);
			return;
		}
		if (RegionManager.LOADED_REGIONS.contains(regionId)) {
			return;
		}
		List<GameObject> defaultObjects = MapRegionParser.parseMap(regionId, keys);
		RegionManager.LOADED_REGIONS.add(regionId);
		if (defaultObjects.isEmpty()) {
			return;
		}
		final int regionX = (regionId >> 8) * 64;
		final int regionY = (regionId & 0xff) * 64;
		defaultObjects.forEach(object -> spawnObject(object, new int[] { object.getLocation().getX() - regionX, object.getLocation().getY() - regionY }));
	}
	
	/**
	 * Gets the region map, if it isn't set we create a new one that isn't clipped only
	 */
	public RegionMap forceGetRegionMap() {
		if (map == null) {
			map = new RegionMap(regionId, false);
		}
		return map;
	}
	
	/**
	 * Gets the clipped only region map, if it isn't set we create a new oen that isn't clipped only
	 */
	public RegionMap forceGetRegionMapClipedOnly() {
		if (clipedOnlyMap == null) {
			clipedOnlyMap = new RegionMap(regionId, true);
		}
		return clipedOnlyMap;
	}
	
	/**
	 * Gets the mask
	 *
	 * @param plane
	 * 		The plane
	 * @param localX
	 * 		The local x
	 * @param localY
	 * 		The local y
	 */
	public int getMask(int plane, int localX, int localY) {
		if (map == null || !allLoaded()) {
			return -1; // cliped tile
		}
		return map.getMasks()[plane][localX][localY];
	}
	
	/**
	 * Gets the clipped only mask
	 *
	 * @param plane
	 * 		The plane
	 * @param localX
	 * 		The local x
	 * @param localY
	 * 		The local y
	 */
	public int getMaskClipedOnly(int plane, int localX, int localY) {
		if (clipedOnlyMap == null || !allLoaded()) {
			return -1; // cliped tile
		}
		return clipedOnlyMap.getMasks()[plane][localX][localY];
	}
	
	/**
	 * Adds an entity to this region.
	 *
	 * @param entity
	 * 		The entity to add.
	 */
	public void addEntity(Entity entity) {
		if (entity.isPlayer()) {
			players.add(entity.toPlayer());
		} else if (entity.isNPC()) {
			if (!npcs.contains(entity.toNPC())) {
				npcs.add(entity.toNPC());
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
		checkNPCSpawns();
		checkObjectSpawns();
	}
	
	/**
	 * Checks for all spawns to be done
	 */
	private void checkNPCSpawns() {
		if (loadedFlags[RegionConstants.LOADED_NPCS_FLAG]) {
			return;
		}
		loadedFlags[RegionConstants.LOADED_NPCS_FLAG] = true;
		NPCSpawnRepository.loadSpawns(regionId);
	}
	
	/**
	 * Checks the object spawns
	 */
	private void checkObjectSpawns() {
		if (loadedFlags[RegionConstants.LOADED_OBJECTS_FLAG]) {
			return;
		}
		loadedFlags[RegionConstants.LOADED_OBJECTS_FLAG] = true;
		// TODO: load object spawns
	}
	
	/**
	 * Removes an entity from this region.
	 *
	 * @param entity
	 * 		The entity to remove.
	 */
	public void removeEntity(Entity entity) {
		if (entity.isPlayer()) {
			players.remove(entity.toPlayer());
		} else if (entity.isNPC()) {
			npcs.remove(entity.toNPC());
		}
	}
	
	/**
	 * Adds a game object.
	 *
	 * @param object
	 * 		The object to add.
	 */
	private void addDefaultObject(GameObject object) {
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
	private Optional<GameObject> findDefaultGameObject(int objectId, int x, int y, int plane, int type) {
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
	private void refreshAllObjects(Player player) {
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
	public boolean removeFloorItem(FloorItem item) {
		if (!floorItems.contains(item)) {
			return false;
		}
		floorItems.remove(item);
		players.stream().filter(player -> player != null && player.getLocation().getPlane() == item.getLocation().getPlane() && player.isRenderable()).forEach(player -> player.getTransmitter().send(new FloorItemRemovalBuilder(item).build(player)));
		return true;
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
	 * Spawns an object
	 *
	 * @param object
	 * 		The object
	 */
	public void spawnObject(GameObject object) {
		spawnObject(object, null);
	}
	
	/**
	 * Spawns an object into the region
	 *
	 * @param object
	 * 		The object
	 * @param defaultObjectData
	 * 		If the object is as default game object
	 */
	private void spawnObject(GameObject object, int[] defaultObjectData) {
		boolean defaultObject = defaultObjectData != null;
		if (defaultObject) {
			if (deleteListContains(object)) {
				return;
			}
			addDefaultObject(object);
			clip(object, defaultObjectData[0], defaultObjectData[1]);
			return;
		} else {
			Optional<GameObject> spawnedOptional = findSpawnedGameObject(object.getId(), object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getPlane(), object.getType());
			if (spawnedOptional.isPresent()) {
				final GameObject spawned = spawnedOptional.get();
				spawnedObjects.remove(spawned);
				removedObjects.add(spawned);
				unclip(spawned, spawned.getLocation().getXInRegion(), spawned.getLocation().getY());
			}
			spawnedObjects.add(object);
			object.setSpawnType(ObjectType.SERVER);
		}
		clip(object, object.getLocation().getXInRegion(), object.getLocation().getYInRegion());
		players.forEach(this::refreshAllObjects);
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
	 * Clips an object
	 *
	 * @param object
	 * 		The object
	 * @param localX
	 * 		The local x of the object
	 * @param localY
	 * 		The local y of the object
	 */
	private void clip(GameObject object, int localX, int localY) {
		if (map == null) {
			map = new RegionMap(regionId, false);
		}
		if (clipedOnlyMap == null) {
			clipedOnlyMap = new RegionMap(regionId, true);
		}
		int plane = object.getLocation().getPlane();
		int type = object.getType();
		int rotation = object.getRotation();
		if (localX < 0 || localY < 0 || localX >= map.getMasks()[plane].length || localY >= map.getMasks()[plane][localX].length) {
			return;
		}
		ObjectDefinition objectDefinition = object.getDefinitions();
		if (type == 22 ? objectDefinition.getActionCount() != 1 : objectDefinition.getActionCount() == 0) {
			return;
		}
		if (type >= 0 && type <= 3) {
			if (!objectDefinition.isClippingFlag()) {
				map.addWall(plane, localX, localY, type, rotation, objectDefinition.isSolid(), !objectDefinition.isClippingFlag());
			}
			if (objectDefinition.isSolid()) {
				clipedOnlyMap.addWall(plane, localX, localY, type, rotation, objectDefinition.isSolid(), !objectDefinition.isClippingFlag());
			}
		} else if (type >= 9 && type <= 21) {
			int sizeX;
			int sizeY;
			if (rotation != 1 && rotation != 3) {
				sizeX = objectDefinition.getSizeX();
				sizeY = objectDefinition.getSizeY();
			} else {
				sizeX = objectDefinition.getSizeY();
				sizeY = objectDefinition.getSizeX();
			}
			map.addObject(plane, localX, localY, sizeX, sizeY, objectDefinition.isSolid(), !objectDefinition.isClippingFlag());
			if (objectDefinition.isSolid()) {
				clipedOnlyMap.addObject(plane, localX, localY, sizeX, sizeY, objectDefinition.isSolid(), !objectDefinition.isClippingFlag());
			}
		} else if (type == 22) {
			map.addFloor(plane, localX, localY);
		}
	}
	
	/**
	 * Unclips a game object
	 *
	 * @param object
	 * 		The object
	 * @param localX
	 * 		The local x of the object
	 * @param localY
	 * 		The local y of the object
	 */
	public void unclip(GameObject object, int localX, int localY) {
		if (map == null) {
			map = new RegionMap(regionId, false);
		}
		if (clipedOnlyMap == null) {
			clipedOnlyMap = new RegionMap(regionId, true);
		}
		int plane = object.getLocation().getPlane();
		int type = object.getType();
		int rotation = object.getRotation();
		if (localX < 0 || localY < 0 || localX >= map.getMasks()[plane].length || localY >= map.getMasks()[plane][localX].length) {
			return;
		}
		ObjectDefinition objectDefinition = object.getDefinitions();
		
		if (type == 22 ? objectDefinition.getActionCount() != 1 : objectDefinition.getActionCount() == 0) {
			return;
		}
		if (type >= 0 && type <= 3) {
			map.removeWall(plane, localX, localY, type, rotation, objectDefinition.isSolid(), !objectDefinition.isClippingFlag());
			if (objectDefinition.isSolid()) {
				clipedOnlyMap.removeWall(plane, localX, localY, type, rotation, objectDefinition.isSolid(), !objectDefinition.isClippingFlag());
			}
		} else if (type >= 9 && type <= 21) {
			int sizeX;
			int sizeY;
			if (rotation != 1 && rotation != 3) {
				sizeX = objectDefinition.getSizeX();
				sizeY = objectDefinition.getSizeY();
			} else {
				sizeX = objectDefinition.getSizeY();
				sizeY = objectDefinition.getSizeX();
			}
			map.removeObject(plane, localX, localY, sizeX, sizeY, objectDefinition.isSolid(), !objectDefinition.isClippingFlag());
			if (objectDefinition.isSolid()) {
				clipedOnlyMap.removeObject(plane, localX, localY, sizeX, sizeY, objectDefinition.isSolid(), !objectDefinition.isClippingFlag());
			}
		} else if (type == 22) {
			map.removeFloor(plane, localX, localY);
		}
	}
	
	/**
	 * Removes an object
	 *
	 * @param object
	 * 		The object
	 */
	public void removeObject(GameObject object) {
		if (object.getSpawnType() == ObjectType.SERVER) {
			spawnedObjects.remove(object);
		}
		removedObjects.add(object);
		unclip(object, object.getLocation().getXInRegion(), object.getLocation().getYInRegion());
		players.forEach(this::refreshAllObjects);
	}
	
	/**
	 * Checks if all the data has loaded
	 */
	public boolean allLoaded() {
		for (boolean flag : loadedFlags) {
			if (!flag) {
				return false;
			}
		}
		return true;
	}
	
}
package org.redrune.game.global.map.region;

import com.alex.io.InputStream;
import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.Cache;
import org.redrune.cache.loaders.ObjectDefinitions;
import org.redrune.engine.SystemManager;
import org.redrune.game.GameFlags;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.link.MusicManager;
import org.redrune.game.entity.item.FloorItem;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.World;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.entity.object.ObjectRemoval;
import org.redrune.utility.game.entity.object.ObjectSpawning;
import org.redrune.utility.game.map.MapArchiveKeys;
import org.redrune.utility.game.repository.npc.spawn.NPCSpawnRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Region {
	
	public static final int[] OBJECT_SLOTS = new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 };
	
	public static final int OBJECT_SLOT_WALL = 0;
	
	public static final int OBJECT_SLOT_WALL_DECORATION = 1;
	
	public static final int OBJECT_SLOT_FLOOR = 2;
	
	public static final int OBJECT_SLOT_FLOOR_DECORATION = 3;
	
	protected int regionId;
	
	protected RegionMap map;
	
	protected RegionMap clipedOnlyMap;
	
	protected List<Integer> playersIndexes;
	
	protected List<Integer> npcsIndexes;
	
	protected List<WorldObject> spawnedObjects;
	
	protected List<WorldObject> removedObjects;
	
	protected WorldObject[][][][] objects;
	
	private List<FloorItem> floorItems;
	
	private volatile int loadMapStage;
	
	@Getter
	private boolean loadedNPCSpawns;
	
	@Getter
	private boolean loadedObjectSpawns;
	
	@Getter
	private boolean loadedItemSpawns;
	
	@Setter
	private int[] musicIds;
	
	public Region(int regionId) {
		this.regionId = regionId;
		this.spawnedObjects = new CopyOnWriteArrayList<>();
		this.removedObjects = new CopyOnWriteArrayList<>();
		MusicManager.loadMusicIds(this);
	}
	
	public void checkLoadMap() {
		if (getLoadMapStage() == 0) {
			setLoadMapStage(1);
			SystemManager.SLOW_EXECUTOR.execute(() -> {
				try {
					loadRegionMap();
					setLoadMapStage(2);
					if (!isLoadedObjectSpawns()) {
						loadObjectSpawns();
						setLoadedObjectSpawns(true);
					}
					if (!isLoadedNPCSpawns()) {
						loadNPCSpawns();
						setLoadedNPCSpawns(true);
					}
					if (!isLoadedItemSpawns()) {
						loadItemSpawns();
						setLoadedItemSpawns(true);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	public int getLoadMapStage() {
		return loadMapStage;
	}
	
	public void loadRegionMap() {
		int regionX = (regionId >> 8) * 64;
		int regionY = (regionId & 0xff) * 64;
		int landArchiveId = Cache.STORE.getIndexes()[5].getArchiveId("l" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));
		byte[] landContainerData = landArchiveId == -1 ? null : Cache.STORE.getIndexes()[5].getFile(landArchiveId, 0, MapArchiveKeys.getKey(regionId));
		int mapArchiveId = Cache.STORE.getIndexes()[5].getArchiveId("m" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));
		byte[] mapContainerData = mapArchiveId == -1 ? null : Cache.STORE.getIndexes()[5].getFile(mapArchiveId, 0);
		byte[][][] mapSettings = mapContainerData == null ? null : new byte[4][64][64];
		if (mapContainerData != null) {
			InputStream mapStream = new InputStream(mapContainerData);
			for (int plane = 0; plane < 4; plane++) {
				for (int x = 0; x < 64; x++) {
					for (int y = 0; y < 64; y++) {
						while (true) {
							int value = mapStream.readUnsignedByte();
							if (value == 0) {
								break;
							} else if (value == 1) {
								mapStream.readByte();
								break;
							} else if (value <= 49) {
								mapStream.readByte();
								
							} else if (value <= 81) {
								mapSettings[plane][x][y] = (byte) (value - 49);
							}
						}
					}
				}
			}
			for (int plane = 0; plane < 4; plane++) {
				for (int x = 0; x < 64; x++) {
					for (int y = 0; y < 64; y++) {
						if ((mapSettings[plane][x][y] & 1) == 1) {
							int height = plane;
							if ((mapSettings[1][x][y] & 2) == 2) {
								height--;
							}
							if (height >= 0 && height <= 3) {
								forceGetRegionMap().addUnwalkable(height, x, y);
							}
						}
					}
				}
			}
			if (landContainerData != null) {
				InputStream landStream = new InputStream(landContainerData);
				int objectId = -1;
				int incr;
				while ((incr = landStream.readSmart2()) != 0) {
					objectId += incr;
					int location = 0;
					int incr2;
					while ((incr2 = landStream.readUnsignedSmart()) != 0) {
						location += incr2 - 1;
						int localX = (location >> 6 & 0x3f);
						int localY = (location & 0x3f);
						int plane = location >> 12;
						int objectData = landStream.readUnsignedByte();
						int type = objectData >> 2;
						int rotation = objectData & 0x3;
						if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
							continue;
						}
						int objectPlane = plane;
						if (mapSettings != null && (mapSettings[1][localX][localY] & 2) == 2) {
							objectPlane--;
						}
						if (objectPlane < 0 || objectPlane >= 4 || plane < 0 || plane >= 4) {
							continue;
						}
						spawnObject(new WorldObject(objectId, type, rotation, localX + regionX, localY + regionY, objectPlane), objectPlane, localX, localY, true);
					}
				}
			}
			if (GameFlags.debugMode && landContainerData == null && landArchiveId != -1 && MapArchiveKeys.getKey(regionId) != null) {
				System.out.println("Missing xteas for region " + regionId + ".");
			}
		}
	}
	
	private void loadObjectSpawns() {
		ObjectSpawning.loadObjectSpawns(regionId);
		refreshPlayerObjects();
	}
	
	private void loadNPCSpawns() {
		NPCSpawnRepository.loadSpawns(regionId);
	}
	
	private void loadItemSpawns() {
		//ItemSpawns.loadItemSpawns(regionId);
	}
	
	private void refreshPlayerObjects() {
		List<Integer> playerIndexes = getPlayerIndexes();
		if (playerIndexes == null) {
			return;
		}
		for (int playerIndex : playerIndexes) {
			Player player = World.getPlayers().get(playerIndex);
			if (player == null || !player.hasStarted() || player.isFinished()) {
				continue;
			}
			player.getPackets().refreshSpawnedObjects();
		}
	}
	
	public RegionMap forceGetRegionMap() {
		if (map == null) {
			map = new RegionMap(regionId, false);
		}
		return map;
	}
	
	public void spawnObject(WorldObject object, int plane, int localX, int localY, boolean original) {
		if (objects == null) {
			objects = new WorldObject[4][64][64][4];
		}
		WorldObject customRemovedObject = ObjectRemoval.removedObjectExists(object);
		int slot = OBJECT_SLOTS[object.getType()];
		if (original) {
			if (customRemovedObject != null) {
				unclip(object.getPlane(), object.getXInRegion(), object.getYInRegion());
				removedObjects.add(object);
				return;
			}
			objects[plane][localX][localY][slot] = object;
			clip(object, localX, localY);
		} else {
			WorldObject spawned = getSpawnedObjectWithSlot(plane, localX, localY, slot);
			// found non original object on this slot. removing it since we
			// replacing with a new non original
			if (spawned != null) {
				object.setSpawned(false);
				spawnedObjects.remove(spawned);
				// unclips non orignal old object which had been cliped so can
				// clip the new non original
				unclip(spawned, localX, localY);
			}
			WorldObject removed = getRemovedObjectWithSlot(plane, localX, localY, slot);
			// there was a original object removed. lets readd it
			if (removed != null) {
				// we only spawn the removed object if there is no custom removed object
				// on this tile
				if (customRemovedObject == null) {
					customRemovedObject = ObjectRemoval.removedObjectExists(removed);
				}
				// making sure that the removed object on this tile isnt the startup removed object
				if (customRemovedObject == null) {
					object = removed;
					removedObjects.remove(object);
					object.setSpawned(false);
				}
				// if an object wasn't removed on this tile, we can spawn it
				if (customRemovedObject != null) {
					spawnedObjects.add(object);
					object.setSpawned(true);
				}
				// adding non original object to this place
			} else if (objects[plane][localX][localY][slot] != object) {
				spawnedObjects.add(object);
				object.setSpawned(true);
				// unclips orignal old object which had been cliped so can clip
				// the new non original
				if (objects[plane][localX][localY][slot] != null) {
					unclip(objects[plane][localX][localY][slot], localX, localY);
				}
			} else if (spawned == null) {
				if (GameFlags.debugMode) {
					System.out.println("Requested object to spawn is already spawned.(Shouldnt happen)");
				}
				return;
			}
			// clips spawned object(either original or non original)
			clip(object, localX, localY);
			for (Player p2 : World.getPlayers()) {
				if (p2 == null || !p2.hasStarted() || p2.isFinished() || !p2.getMapRegionsIds().contains(regionId)) {
					continue;
				}
				p2.getPackets().sendSpawnedObject(object);
			}
		}
	}
	
	public void clip(WorldObject object, int x, int y) {
		if (map == null) {
			map = new RegionMap(regionId, false);
		}
		if (clipedOnlyMap == null) {
			clipedOnlyMap = new RegionMap(regionId, true);
		}
		int plane = object.getPlane();
		int type = object.getType();
		int rotation = object.getRotation();
		if (x < 0 || y < 0 || x >= map.getMasks()[plane].length || y >= map.getMasks()[plane][x].length) {
			return;
		}
		ObjectDefinitions objectDefinition = ObjectDefinitions.getObjectDefinitions(object.getId()); // load
		// here
		
		if (type == 22 ? objectDefinition.getClipType() != 1 : objectDefinition.getClipType() == 0) {
			return;
		}
		if (type >= 0 && type <= 3) {
			if (!objectDefinition.isIgnoreClipOnAlternativeRoute()) //disabled those walls for now since theyre guard corners, temporary fix
			{
				map.addWall(plane, x, y, type, rotation, objectDefinition.isProjectileClipped(), !objectDefinition.isIgnoreClipOnAlternativeRoute());
			}
			if (objectDefinition.isProjectileClipped()) {
				clipedOnlyMap.addWall(plane, x, y, type, rotation, objectDefinition.isProjectileClipped(), !objectDefinition.isIgnoreClipOnAlternativeRoute());
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
			map.addObject(plane, x, y, sizeX, sizeY, objectDefinition.isProjectileClipped(), !objectDefinition.isIgnoreClipOnAlternativeRoute());
			if (objectDefinition.isProjectileClipped()) {
				clipedOnlyMap.addObject(plane, x, y, sizeX, sizeY, objectDefinition.isProjectileClipped(), !objectDefinition.isIgnoreClipOnAlternativeRoute());
			}
		} else if (type == 22) {
			map.addFloor(plane, x, y); // dont ever fucking think about removing it..., some floor deco objects DOES BLOCK WALKING
		}
	}
	
	public WorldObject getSpawnedObjectWithSlot(int plane, int x, int y, int slot) {
		for (WorldObject object : spawnedObjects) {
			if (object.getXInRegion() == x && object.getYInRegion() == y && object.getPlane() == plane && OBJECT_SLOTS[object.getType()] == slot) {
				return object;
			}
		}
		return null;
	}
	
	public void unclip(WorldObject object, int x, int y) {
		if (map == null) {
			map = new RegionMap(regionId, false);
		}
		if (clipedOnlyMap == null) {
			clipedOnlyMap = new RegionMap(regionId, true);
		}
		int plane = object.getPlane();
		int type = object.getType();
		int rotation = object.getRotation();
		if (x < 0 || y < 0 || x >= map.getMasks()[plane].length || y >= map.getMasks()[plane][x].length) {
			return;
		}
		ObjectDefinitions objectDefinition = ObjectDefinitions.getObjectDefinitions(object.getId()); // load
		// here
		if (type == 22 ? objectDefinition.getClipType() != 1 : objectDefinition.getClipType() == 0) {
			return;
		}
		if (type >= 0 && type <= 3) {
			map.removeWall(plane, x, y, type, rotation, objectDefinition.isProjectileClipped(), !objectDefinition.isIgnoreClipOnAlternativeRoute());
			if (objectDefinition.isProjectileClipped()) {
				clipedOnlyMap.removeWall(plane, x, y, type, rotation, objectDefinition.isProjectileClipped(), !objectDefinition.isIgnoreClipOnAlternativeRoute());
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
			map.removeObject(plane, x, y, sizeX, sizeY, objectDefinition.isProjectileClipped(), !objectDefinition.isIgnoreClipOnAlternativeRoute());
			if (objectDefinition.isProjectileClipped()) {
				clipedOnlyMap.removeObject(plane, x, y, sizeX, sizeY, objectDefinition.isProjectileClipped(), !objectDefinition.isIgnoreClipOnAlternativeRoute());
			}
		} else if (type == 22) {
			map.removeFloor(plane, x, y);
		}
	}
	
	public WorldObject getRemovedObjectWithSlot(int plane, int x, int y, int slot) {
		for (WorldObject object : removedObjects) {
			if (object.getXInRegion() == x && object.getYInRegion() == y && object.getPlane() == plane && OBJECT_SLOTS[object.getType()] == slot) {
				return object;
			}
		}
		return null;
	}
	
	public void setLoadedItemSpawns(boolean loadedItemSpawns) {
		this.loadedItemSpawns = loadedItemSpawns;
	}
	
	public void setLoadedNPCSpawns(boolean loadedNPCSpawns) {
		this.loadedNPCSpawns = loadedNPCSpawns;
	}
	
	public void setLoadedObjectSpawns(boolean loadedObjectSpawns) {
		this.loadedObjectSpawns = loadedObjectSpawns;
	}
	
	public void setLoadMapStage(int loadMapStage) {
		this.loadMapStage = loadMapStage;
	}
	
	/**
	 * Unload's map from memory.
	 */
	public void removeMapFromMemory() {
		if (getLoadMapStage() == 2 && (playersIndexes == null || playersIndexes.isEmpty()) && (npcsIndexes == null || npcsIndexes.isEmpty())) {
			objects = null;
			map = null;
			setLoadMapStage(0);
		}
	}
	
	public RegionMap forceGetRegionMapClipedOnly() {
		if (clipedOnlyMap == null) {
			clipedOnlyMap = new RegionMap(regionId, true);
		}
		return clipedOnlyMap;
	}
	
	public RegionMap getRegionMap() {
		return map;
	}
	
	public int getMask(int plane, int localX, int localY) {
		if (map == null || getLoadMapStage() != 2) {
			return -1; // cliped tile
		}
		return map.getMasks()[plane][localX][localY];
	}
	
	public int getMaskClipedOnly(int plane, int localX, int localY) {
		if (clipedOnlyMap == null || getLoadMapStage() != 2) {
			return -1; // cliped tile
		}
		return clipedOnlyMap.getMasks()[plane][localX][localY];
	}
	
	public void setMask(int plane, int localX, int localY, int mask) {
		if (map == null || getLoadMapStage() != 2) {
			return; // cliped tile
		}
		
		if (localX >= 64 || localY >= 64 || localX < 0 || localY < 0) {
			WorldTile tile = new WorldTile(map.getRegionX() + localX, map.getRegionY() + localY, plane);
			int regionId = tile.getRegionId();
			int newRegionX = (regionId >> 8) * 64;
			int newRegionY = (regionId & 0xff) * 64;
			RegionManager.getRegion(tile.getRegionId()).setMask(plane, tile.getX() - newRegionX, tile.getY() - newRegionY, mask);
			return;
		}
		
		map.setMask(plane, localX, localY, mask);
	}
	
	public void unclip(int plane, int x, int y) {
		if (map == null) {
			map = new RegionMap(regionId, false);
		}
		if (clipedOnlyMap == null) {
			clipedOnlyMap = new RegionMap(regionId, true);
		}
		map.setMask(plane, x, y, 0);
	}
	
	public WorldObject getObject(int plane, int x, int y) {
		WorldObject[] objects = getObjects(plane, x, y);
		if (objects == null) {
			return null;
		}
		return objects[0];
	}
	
	public WorldObject[] getObjects(int plane, int x, int y) {
		if (objects == null) {
			return null;
		}
		return objects[plane][x][y];
	}
	
	public List<WorldObject> getObjects() {
		if (objects == null) {
			return null;
		}
		List<WorldObject> list = new ArrayList<WorldObject>();
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++) {
					if (objects[z][x][y] == null) {
						continue;
					}
					for (WorldObject o : objects[z][x][y]) {
						if (o != null) {
							list.add(o);
						}
					}
				}
			}
		}
		return list;
	}
	
	public void removeObject(WorldObject object, int plane, int localX, int localY) {
		if (objects == null) {
			objects = new WorldObject[4][64][64][4];
		}
		int slot = OBJECT_SLOTS[object.getType()];
		WorldObject removed = getRemovedObjectWithSlot(plane, localX, localY, slot);
		if (removed != null) {
			removedObjects.remove(object);
			clip(removed, localX, localY);
		}
		WorldObject original = null;
		// found non original object on this slot. removing it since we
		// replacing with real one or none if none
		WorldObject spawned = getSpawnedObjectWithSlot(plane, localX, localY, slot);
		// finding if theres a custom object on this tile
		WorldObject customObject = ObjectRemoval.removedObjectExists(object);
		if (spawned != null) {
			object = spawned;
			spawnedObjects.remove(object);
			unclip(object, localX, localY);
			object.setSpawned(false);
			if (objects[plane][localX][localY][slot] != null) {// original
				// unclips non original to clip original above
				clip(objects[plane][localX][localY][slot], localX, localY);
				original = objects[plane][localX][localY][slot];
			}
			// found original object on this slot. removing it since requested
		} else if (objects[plane][localX][localY][slot] == object) { // removes  original
			unclip(object, localX, localY);
			removedObjects.add(object);
		} else if (customObject != null) {
			unclip(object, localX, localY);
			removedObjects.add(object);
		} else {
			if (GameFlags.debugMode) {
				System.out.println("Requested object to remove wasnt found.(Shouldnt happen)");
			}
			return;
		}
		for (Player p2 : World.getPlayers()) {
			if (p2 == null || !p2.hasStarted() || p2.isFinished() || !p2.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			if (original != null) {
				p2.getPackets().sendSpawnedObject(original);
			} else {
				p2.getPackets().sendDestroyObject(object);
			}
		}
	}
	
	public WorldObject getStandardObject(int plane, int x, int y) {
		return getObjectWithSlot(plane, x, y, OBJECT_SLOT_FLOOR);
	}
	
	public WorldObject getObjectWithSlot(int plane, int x, int y, int slot) {
		if (objects == null) {
			return null;
		}
		WorldObject o = getSpawnedObjectWithSlot(plane, x, y, slot);
		if (o == null) {
			if (getRemovedObjectWithSlot(plane, x, y, slot) != null) {
				return null;
			}
			return objects[plane][x][y][slot];
		}
		return o;
	}
	
	public WorldObject getObjectWithType(int plane, int x, int y, int type) {
		WorldObject object = getObjectWithSlot(plane, x, y, OBJECT_SLOTS[type]);
		return object != null && object.getType() == type ? object : null;
	}
	
	public WorldObject[] getAllObjects(int plane, int x, int y) {
		if (objects == null) {
			return null;
		}
		return objects[plane][x][y];
	}
	
	public List<WorldObject> getAllObjects() {
		if (objects == null) {
			return null;
		}
		List<WorldObject> list = new ArrayList<WorldObject>();
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++) {
					if (objects[z][x][y] == null) {
						continue;
					}
					for (WorldObject o : objects[z][x][y]) {
						if (o != null) {
							list.add(o);
						}
					}
				}
			}
		}
		return list;
	}
	
	public boolean containsObjectWithId(int plane, int x, int y, int id) {
		WorldObject object = getObjectWithId(plane, x, y, id);
		return object != null && object.getId() == id;
	}
	
	public WorldObject getObjectWithId(int plane, int x, int y, int id) {
		if (objects == null) {
			return null;
		}
		for (WorldObject object : removedObjects) {
			if (object.getId() == id && object.getXInRegion() == x && object.getYInRegion() == y && object.getPlane() == plane) {
				return null;
			}
		}
		for (int i = 0; i < 4; i++) {
			WorldObject object = objects[plane][x][y][i];
			if (object != null && object.getId() == id) {
				WorldObject spawned = getSpawnedObjectWithSlot(plane, x, y, OBJECT_SLOTS[object.getType()]);
				return spawned == null ? object : null;
			}
		}
		for (WorldObject object : spawnedObjects) {
			if (object.getXInRegion() == x && object.getYInRegion() == y && object.getPlane() == plane && object.getId() == id) {
				return object;
			}
		}
		return null;
	}
	
	public WorldObject getObjectWithId(int id, int plane) {
		if (objects == null) {
			return null;
		}
		for (WorldObject object : spawnedObjects) {
			if (object.getId() == id && object.getPlane() == plane) {
				return object;
			}
		}
		for (int x = 0; x < 64; x++) {
			for (int y = 0; y < 64; y++) {
				for (int slot = 0; slot < objects[plane][x][y].length; slot++) {
					WorldObject object = objects[plane][x][y][slot];
					if (object != null && object.getId() == id) {
						return object;
					}
				}
			}
		}
		return null;
	}
	
	public List<WorldObject> getSpawnedObjects() {
		return spawnedObjects;
	}
	
	public List<WorldObject> getRemovedObjects() {
		return removedObjects;
	}
	
	public int getRotation(int plane, int x, int y) {
		return 0;
	}
	
	/**
	 * Get's ground item with specific id on the specific location in this region.
	 */
	public FloorItem getGroundItem(int id, WorldTile tile, Player player) {
		if (floorItems == null) {
			return null;
		}
		for (FloorItem item : floorItems) {
			if ((item.isInvisible()) && (item.hasOwner() && !player.getUsername().equals(item.getOwner().getUsername()))) {
				continue;
			}
			if (item.getId() == id && tile.getX() == item.getTile().getX() && tile.getY() == item.getTile().getY() && tile.getPlane() == item.getTile().getPlane()) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * Return's list of ground items that are currently loaded. List may be null if there's no ground items. Modifying
	 * given list is prohibited.
	 */
	public List<FloorItem> getFloorItems() {
		return floorItems;
	}
	
	/**
	 * Return's list of ground items that are currently loaded. This method ensures that returned list is not null.
	 * Modifying given list is prohibited.
	 */
	public List<FloorItem> forceGetFloorItems() {
		if (floorItems == null) {
			floorItems = new CopyOnWriteArrayList<>();
		}
		return floorItems;
	}
	
	public List<Integer> getPlayerIndexes() {
		return playersIndexes;
	}
	
	public int getPlayerCount() {
		return playersIndexes == null ? 0 : playersIndexes.size();
	}
	
	public List<Integer> getNPCsIndexes() {
		return npcsIndexes;
	}
	
	public void addPlayerIndex(int index) {
		// creates list if doesnt exist
		if (playersIndexes == null) {
			playersIndexes = new CopyOnWriteArrayList<>();
		}
		playersIndexes.add(index);
	}
	
	public void addNPCIndex(int index) {
		// creates list if doesnt exist
		if (npcsIndexes == null) {
			npcsIndexes = new CopyOnWriteArrayList<>();
		}
		npcsIndexes.add(index);
	}
	
	public void removePlayerIndex(Integer index) {
		if (playersIndexes == null) // removed region example cons or dung
		{
			return;
		}
		playersIndexes.remove(index);
	}
	
	public boolean removeNPCIndex(Object index) {
		if (npcsIndexes == null) // removed region example cons or dung
		{
			return false;
		}
		return npcsIndexes.remove(index);
	}
	
	public int getMusicId() {
		if (musicIds == null) {
			return -1;
		}
		if (musicIds.length == 1) {
			return musicIds[0];
		}
		return musicIds[Misc.getRandom(musicIds.length - 1)];
	}
	
	public int getRegionId() {
		return regionId;
	}
	
	public WorldObject getSpawnedObject(WorldTile tile) {
		if (spawnedObjects == null) {
			return null;
		}
		for (WorldObject object : spawnedObjects) {
			if (object.getX() == tile.getX() && object.getY() == tile.getY() && object.getPlane() == tile.getPlane()) {
				return object;
			}
		}
		return null;
	}
	
}
package org.redrune.rs2.world.map.region;

import lombok.Getter;
import org.redrune.rs2.node.entity.Entity;
import org.redrune.rs2.node.entity.npc.NPC;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.object.GameObject;
import org.redrune.rs2.world.map.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Emperor
 * @author Dementhium development team (mainly).
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class Region {
	
	public static final int PLAYERS = 0, NPCS = 1;
	
	public static final int REGION_SIZE = 128;
	
	public static final int MAX_MAP_X = 16383, MAX_MAP_Y = 16383;
	
	private static Region[][] regions = new Region[(MAX_MAP_X + 1) / REGION_SIZE][(MAX_MAP_Y + 1) / REGION_SIZE];
	
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
	private final List<Player>[] players = new LinkedList[4];
	
	/**
	 * A list of NPCs in this region.
	 */
	@SuppressWarnings("unchecked")
	private final List<NPC>[] npcs = new LinkedList[4];
	
	/**
	 * The id of the region
	 */
	private final int id;
	
	/**
	 * The clipping masks of this region.
	 */
	private int[][][] clippingMasks = new int[4][][];
	
	/**
	 * The locations in this region.
	 */
	private Location[][][] tiles = new Location[4][][];
	
	/**
	 * The region size.
	 */
	private int size;
	
	/**
	 * The base x-coordinate of this region.
	 */
	private int x;
	
	/**
	 * The base y-coordinate of this region.
	 */
	private int y;
	
	/**
	 * A list of game objects on this region.
	 */
	@Getter
	private List<GameObject> objects;
	
	/**
	 * The clipped flag.
	 */
	private boolean clipped;
	
	/**
	 * Constructs a new {@code Region} {@code Object}.
	 *
	 * @param x
	 * 		The base x-coordinate.
	 * @param y
	 * 		The base y-coordinate.
	 * @param size
	 * 		The size of the region.
	 */
	public Region(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		for (int i = 0; i < 4; i++) {
			players[i] = new LinkedList<>();
			npcs[i] = new LinkedList<>();
		}
		this.id = Location.create(x, y, 0).getRegionId();
	}
	
	@Override
	public int hashCode() {
		return x << 8 | y;
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ", players=" + Arrays.toString(players) + ", npcs=" + Arrays.toString(npcs) + "]";
	}
	
	public static boolean isPassable(Location l) {
		int clippingMask = getClippingMask(l.getX(), l.getY(), l.getZ());
		if (clippingMask == -1) {
			return true; //?
		}
		return clippingMask < 1;
	}
	
	public static int getClippingMask(int x, int y, int z) {
		Region region = forCoords(x, y);
		if (region.clippingMasks[z] == null || !region.clipped) {
			return -1;
		}
		int localX = x - ((x >> 7) << 7);
		int localY = y - ((y >> 7) << 7);
		return region.clippingMasks[z][localX][localY];
	}
	
	public static Region forCoords(int x, int y) {
		int regionX = x >> 7, regionY = y >> 7;
		Region r = regions[regionX][regionY];
		if (r == null) {
			r = regions[regionX][regionY] = new Region(regionX, regionY, REGION_SIZE);
		}
		return r;
	}
	
	/**
	 * Gets a local location.
	 *
	 * @param x
	 * 		The x-coordinate.
	 * @param y
	 * 		The y-coordinate.
	 * @param z
	 * 		The height.
	 * @return The location.
	 */
	public Location getLocalLocation(int x, int y, int z) {
		if (tiles[z] == null) {
			tiles[z] = new Location[size][size];
		}
		Location tile = tiles[z][x][y];
		if (tile == null) {
			tile = new Location(this.x << 7 | x, this.y << 7 | y, z);
			tiles[z][x][y] = tile;
		}
		return tile;
	}
	
	/**
	 * Adds an entity to this region.
	 *
	 * @param entity
	 * 		The entity to add.
	 */
	public void addEntity(Entity entity) {
		int z = entity.getLocation().getZ();
		if (entity.isPlayer()) {
			synchronized (playerLock) {
				if (!players[z].contains(entity)) {
					players[z].add(entity.toPlayer());
				}
			}
		} else if (entity.isNPC()) {
			synchronized (npcLock) {
				if (!npcs[z].contains(entity)) {
					npcs[z].add(entity.toNPC());
				}
			}
		}
	}
	
	/**
	 * Removes an entity from this region.
	 *
	 * @param entity
	 * 		The entity to remove.
	 */
	public void removeEntity(Entity entity) {
		int z = entity.getLocation().getZ();
		if (entity.isPlayer()) {
			synchronized (playerLock) {
				players[z].remove(entity.toPlayer());
			}
		} else if (entity.isNPC()) {
			synchronized (npcLock) {
				npcs[z].remove(entity.toNPC());
			}
		}
	}
	
	/**
	 * Gets the region size.
	 *
	 * @return The size.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Gets the region base x-coordinate..
	 *
	 * @return The region base x-coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the region base y-coordinate.
	 *
	 * @return The base y-coordinate.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Gets the clipped flag.
	 *
	 * @return The clipping flag.
	 */
	public boolean isClipped() {
		return clipped;
	}
	
	/**
	 * Sets the clipped flag.
	 *
	 * @param clipped
	 * 		The flag to set.
	 */
	public void setClipped(boolean clipped) {
		this.clipped = clipped;
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
	public List<Player>[] getPlayers() {
		synchronized (playerLock) {
			return players;
		}
	}
	
	/**
	 * @return the npcs
	 */
	public List<NPC>[] getNpcs() {
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

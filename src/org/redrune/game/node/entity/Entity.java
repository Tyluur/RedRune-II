package org.redrune.game.node.entity;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.Location;
import org.redrune.game.node.Node;
import org.redrune.game.node.entity.data.WalkingQueue;
import org.redrune.game.node.entity.player.render.UpdateMasks;
import org.redrune.game.node.entity.player.render.flag.impl.FaceEntityUpdate;
import org.redrune.game.world.region.Region;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public abstract class Entity extends Node implements EntityDetails {
	
	/**
	 * The {@code HitMap} {@code Object} instance for this entity
	 */
	@Getter
	private final HitMap hitMap = new HitMap(this);
	
	/**
	 * The index of the entity
	 */
	@Getter
	@Setter
	private transient int index;
	
	/**
	 * The map of temporary attributes
	 */
	private transient ConcurrentHashMap<Object, Object> attributes;
	
	/**
	 * The instance of the update masks
	 */
	@Getter
	private transient UpdateMasks updateMasks;
	
	/**
	 * The entity's walking queue.
	 */
	@Getter
	private transient WalkingQueue walkingQueue;
	
	/**
	 * The region ids we are in
	 */
	@Getter
	private transient CopyOnWriteArrayList<Integer> mapRegionsIds;
	
	/**
	 * The last region the player was in.
	 */
	@Getter
	@Setter
	private transient Region lastRegion;
	
	/**
	 * The location the player was at when the maps were last loaded
	 */
	@Getter
	@Setter
	private transient Location lastLoadedLocation;
	
	/**
	 * Constructs a new {@code Entity}
	 *
	 * @param location
	 * 		The location of the entity
	 */
	protected Entity(Location location) {
		super(location);
	}
	
	/**
	 * Registers all transient variables
	 */
	public void registerTransients() {
		this.updateMasks = new UpdateMasks();
		this.attributes = new ConcurrentHashMap<>();
		this.walkingQueue = new WalkingQueue(this);
		this.mapRegionsIds = new CopyOnWriteArrayList<>();
	}
	
	/**
	 * Gets the attribute from the {@link #attributes} map, and if it doesn't exist, we return the default
	 * value
	 *
	 * @param key
	 * 		The key of the attribute
	 * @param defaultValue
	 * 		The default value
	 * @param <T>
	 * 		The return type
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Object key, T defaultValue) {
		T value = (T) attributes.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	/**
	 * Gets an attribute from the {@link #attributes} map
	 *
	 * @param key
	 * 		The key of the attribute
	 * @param <T>
	 * 		The return type
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Object key) {
		return (T) attributes.get(key);
	}
	
	/**
	 * Puts the key into the attributes map
	 *
	 * @param key
	 * 		The key
	 * @param value
	 * 		The value
	 */
	public <T> T putAttribute(Object key, T value) {
		attributes.put(key, value);
		return value;
	}
	
	/**
	 * Removes an attribute from the {@link #attributes} map
	 *
	 * @param key
	 * 		The key
	 * @param <T>
	 * 		The return type
	 */
	@SuppressWarnings("unchecked")
	public <T> T removeAttribute(Object key) {
		return (T) attributes.remove(key);
	}
	
	/**
	 * Removes an attribute from the {@link #attributes} map, if it doesn't exist, the default value is
	 * returned,
	 *
	 * @param key
	 * 		The key of the attribute to remove
	 * @param defaultValue
	 * 		The default value to return
	 * @param <T>
	 * 		The return type
	 */
	@SuppressWarnings("unchecked")
	public <T> T removeAttribute(Object key, T defaultValue) {
		T value = (T) attributes.remove(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	/**
	 * Loads all the map regions
	 */
	public void loadMapRegions() {
		mapRegionsIds.clear();
		int chunkX = getLocation().getRegionX();
		int chunkY = getLocation().getRegionY();
		int mapHash = Location.VIEWPORT_SIZES[0] >> 4;
		int minRegionX = (chunkX - mapHash) / 8;
		int minRegionY = (chunkY - mapHash) / 8;
		for (int xCalc = minRegionX < 0 ? 0 : minRegionX; xCalc <= ((chunkX + mapHash) / 8); xCalc++) {
			for (int yCalc = minRegionY < 0 ? 0 : minRegionY; yCalc <= ((chunkY + mapHash) / 8); yCalc++) {
				int regionId = yCalc + (xCalc << 8);
				mapRegionsIds.add(regionId);
			}
		}
		lastLoadedLocation = new Location(getLocation().getX(), getLocation().getY(), getLocation().getPlane());
	}
	
	/**
	 * If the client needs a map update.
	 */
	public boolean needsMapUpdate() {
		int lastMapRegionX = lastLoadedLocation.getRegionX();
		int lastMapRegionY = lastLoadedLocation.getRegionY();
		int regionX = getLocation().getRegionX();
		int regionY = getLocation().getRegionY();
		int size = ((Location.VIEWPORT_SIZES[0] >> 3) / 2) - 1;
		return Math.abs(lastMapRegionX - regionX) >= size || Math.abs(lastMapRegionY - regionY) >= size;
	}
	
	/**
	 * Turns this entity to the locked on entity.
	 *
	 * @param lockon
	 * 		The locked on entity.
	 * @return {@code True}.
	 */
	public boolean turnTo(Entity lockon) {
		int index = lockon == null ? -1 : lockon.getClientIndex();
		updateMasks.register(new FaceEntityUpdate(index, isNPC()));
		return true;
	}
	
	/**
	 * Gets the client index of the entity.
	 *
	 * @return The client index.
	 */
	public int getClientIndex() {
		if (isPlayer()) {
			return index + 0x8000;
		}
		return index;
	}
}
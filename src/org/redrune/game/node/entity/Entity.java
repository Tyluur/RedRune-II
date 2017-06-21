package org.redrune.game.node.entity;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.AnimationDefinitionParser;
import org.redrune.cache.parse.definition.AnimationDefinition;
import org.redrune.game.GameFlags;
import org.redrune.game.node.Location;
import org.redrune.game.node.Node;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.UpdateMasks;
import org.redrune.game.node.entity.player.render.flag.impl.Animation;
import org.redrune.game.node.entity.player.render.flag.impl.FaceEntityUpdate;
import org.redrune.game.node.entity.player.render.flag.impl.Graphic;
import org.redrune.game.world.region.Region;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.backend.Priority;

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
	private transient HitMap hitMap;
	
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
	 * The travel manager
	 */
	@Getter
	private transient EntityMovement movement;
	
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
	
	@Override
	public String toString() {
		return "Entity{" + "index=" + index + ", isPlayer=" + isPlayer() + ", lastRegion=" + lastRegion + ", lastLoadedLocation=" + lastLoadedLocation + '}';
	}
	
	/**
	 * Registers all transient variables
	 */
	public void registerTransients() {
		this.hitMap = new HitMap(this);
		this.updateMasks = new UpdateMasks();
		this.attributes = new ConcurrentHashMap<>();
		this.movement = new EntityMovement(this);
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
				/*
				if (World.getRegion(regionId, isPlayer()) instanceof DynamicRegion) {
					isAtDynamicRegion = true;
				}
				 */
				RegionManager.getRegionAndLoad(regionId);
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
	private int getClientIndex() {
		if (isPlayer()) {
			return index + 0x8000;
		}
		return index;
	}
	
	/**
	 * Moves to a location
	 *
	 * @param location
	 * 		The location
	 */
	public void moveTo(Location location) {
		putAttribute(AttributeKey.TELEPORT_LOCATION, location);
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
	 * Sends an animation mask
	 *
	 * @param animationId
	 * 		The id of the animation
	 */
	public void sendAnimation(int animationId) {
		updateMasks.register(new Animation(animationId, 0, isNPC()));
		//	lastAnimationEnd = Utils.currentTimeMillis() + AnimationDefinitions.getAnimationDefinitions(nextAnimation.getIds()[0]).getEmoteTime();
		AnimationDefinition definition = AnimationDefinitionParser.forId(animationId);
		if (definition != null) {
			updateMasks.setLastAnimationEndTime(System.currentTimeMillis() + definition.getEmoteTime());
		}
	}
	
	/**
	 * Sends an animation mask
	 *
	 * @param animationId
	 * 		The id of the animation
	 * @param speed
	 * 		The speed of the animation
	 * @param priority
	 * 		The priority of the animation
	 */
	public void sendAnimation(int animationId, int speed, Priority priority) {
		updateMasks.register(new Animation(animationId, speed, isNPC(), priority));
	}
	
	/**
	 * Sends a graphic mask
	 *
	 * @param graphicsId
	 * 		The id of the graphic
	 */
	public void sendGraphics(int graphicsId) {
		updateMasks.register(new Graphic(graphicsId, 0, 0, isNPC()));
	}
	
	/**
	 * Sends a graphics mask
	 *
	 * @param graphicsId
	 * 		The id of the graphic
	 * @param height
	 * 		The height to send the graphic
	 * @param speed
	 * 		The speed to send the graphic
	 */
	public void sendGraphics(int graphicsId, int height, int speed) {
		updateMasks.register(new Graphic(graphicsId, height, speed, isNPC()));
	}
	
	/**
	 * If we are dead
	 */
	public boolean isDead() {
		return getHitpoints() <= 0;
	}
	
	/**
	 * Gets the world the entity is on
	 */
	public int getWorld() {
		return GameFlags.worldId;
	}
	
	/**
	 * Checks if we are attackable by a player
	 *
	 * @param player
	 * 		The player
	 */
	public boolean attackable(Player player) {
		return true;
	}
	
}
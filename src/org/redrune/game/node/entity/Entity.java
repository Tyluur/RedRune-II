package org.redrune.game.node.entity;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.AnimationDefinitionParser;
import org.redrune.cache.parse.definition.AnimationDefinition;
import org.redrune.core.system.SystemManager;
import org.redrune.game.GameFlags;
import org.redrune.game.node.Location;
import org.redrune.game.node.Node;
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
import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public abstract class Entity extends Node implements EntityDetails {
	
	/**
	 * The combat definitions of the entity. These are saved
	 */
	@Getter
	private final EntityCombatDefinitions combatDefinitions = new EntityCombatDefinitions();
	
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
		this.combatDefinitions.setEntity(this);
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
	 * Teleports to a destination
	 *
	 * @param destination
	 * 		The destination
	 */
	public void teleport(Location destination) {
		putAttribute(AttributeKey.TELEPORT_LOCATION, destination);
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
	 * Sends an animation
	 *
	 * @param animation
	 * 		The animation
	 */
	public void sendAnimation(Animation animation) {
		updateMasks.register(animation);
		//	lastAnimationEnd = Utils.currentTimeMillis() + AnimationDefinitions.getAnimationDefinitions(nextAnimation.getIds()[0]).getEmoteTime();
		AnimationDefinition definition = AnimationDefinitionParser.forId(animation.getId());
		if (definition != null) {
			updateMasks.setLastAnimationEndTime(System.currentTimeMillis() + definition.getEmoteTime());
		}
	}
	
	/**
	 * Sends an animation mask
	 *
	 * @param animationId
	 * 		The id of the animation
	 */
	public void sendAnimation(int animationId) {
		updateMasks.register(new Animation(animationId, 0, isNPC(), Priority.NORMAL));
		//	lastAnimationEnd = Utils.currentTimeMillis() + AnimationDefinitions.getAnimationDefinitions(nextAnimation.getIds()[0]).getEmoteTime();
		AnimationDefinition definition = AnimationDefinitionParser.forId(animationId);
		if (definition != null) {
			updateMasks.setLastAnimationEndTime(System.currentTimeMillis() + definition.getEmoteTime());
		}
	}
	
	/**
	 * Sends an animation that makes sure that we are no longer animation
	 *
	 * @param animationId
	 * 		The animation
	 */
	public void sendAwaitedAnimation(int animationId) {
		updateMasks.register(new Animation(animationId, 0, isNPC(), Priority.LOWEST));
		
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
		if (isPlayer()) {
			return toPlayer().getHealthPoints() <= 0;
		} else {
			return isNPC() && toNPC().getHealthPoints() <= 0;
		}
	}
	
	/**
	 * Gets the world the entity is on
	 */
	public int getWorld() {
		return GameFlags.worldId;
	}
	
	/**
	 * Checks if we are attackable by an entity. // TODO implement slayer requirements in the npc classes.
	 *
	 * @param entity
	 * 		The player
	 */
	public boolean attackable(Entity entity) {
		return true;
	}
	
	/**
	 * Checks if we were in combat recently.
	 */
	public boolean combatRecently() {
		long lastTimeHit = getAttribute(AttributeKey.LAST_TIME_HIT);
		return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastTimeHit) <= 10;
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
	 * Freezes the entity for the amount of ticks
	 *
	 * @param by
	 * 		The entity we were frozen by
	 * @param ticks
	 * 		The amount of ticks
	 * @param message
	 * 		The message to send when we're frozen
	 */
	public void freeze(Entity by, int ticks, String message) {
		// time we will be unfrozen at
		final long frozenUntil = SystemManager.getUpdateWorker().getTicksElapsed() + ticks;
		// storing the time
		putAttribute(AttributeKey.FROZEN_UNTIL, frozenUntil);
		// they can't be frozen again instantly.
		putAttribute(AttributeKey.FREEZE_DELAY, frozenUntil + 6);
		// stores who froze us [16 tile calc]
		putAttribute(AttributeKey.FROZEN_BY, by);
		// sending the message
		if (isPlayer()) {
			toPlayer().getTransmitter().sendMessage(message, false);
		}
		movement.resetWalkSteps();
	}
	
	/**
	 * Entities cannot be frozen instantly after they have once been frozen. The {@link AttributeKey#FREEZE_DELAY}
	 * attribute stores the delay time for said variable.
	 */
	public boolean freezeDelayed() {
		// when freezing is delayed until
		long delay = getAttribute(AttributeKey.FREEZE_DELAY, -1L);
		// the current tick we're on
		long ticks = SystemManager.getUpdateWorker().getTicksElapsed();
		return delay > ticks;
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
	 * Checks if we are frozen
	 */
	public boolean isFrozen() {
		return getAttribute(AttributeKey.FROZEN_UNTIL, -1L) >= SystemManager.getUpdateWorker().getTicksElapsed();
	}
	
	/**
	 * Unfreezes the entity
	 */
	public void unfreeze() {
		removeAttribute(AttributeKey.FROZEN_BY);
		removeAttribute(AttributeKey.FROZEN_UNTIL);
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
	 * Gets the hitpoints of the entity
	 */
	public int getHealthPoints() {
		return (isPlayer() ? toPlayer().getHealthPoints() : toNPC().getHealthPoints());
	}
}
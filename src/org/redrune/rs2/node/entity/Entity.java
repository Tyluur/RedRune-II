package org.redrune.rs2.node.entity;

import java.util.concurrent.ConcurrentHashMap;

import org.redrune.rs2.node.Node;
import org.redrune.rs2.node.entity.data.WalkingQueue;
import org.redrune.rs2.node.entity.npc.NPC;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.entity.player.render.UpdateMasks;
import org.redrune.rs2.world.Location;
import org.redrune.utility.AttributeKey;

import lombok.Getter;
import lombok.Setter;

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
	private transient ConcurrentHashMap<AttributeKey, Object> attributes;
	
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
	}
	
	/**
	 * Verifies if this entity is a player
	 *
	 * @return A {@code Boolean} flag
	 */
	public boolean isPlayer() {
		return toPlayer() != null;
	}
	
	/**
	 * Converts this entity to a {@code Player} {@code Object}
	 *
	 * @return A {@code Player}
	 */
	public Player toPlayer() {
		return null;
	}
	
	/**
	 * Verifies if this entity is an npc
	 *
	 * @return A {@code Boolean} flag
	 */
	public boolean isNPC() {
		return toNPC() != null;
	}
	
	/**
	 * Converts this entity to a {@code NPC} {@code Object}
	 *
	 * @return A {@code NPC}
	 */
	public NPC toNPC() {
		return null;
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
	public <T> T getAttribute(AttributeKey key, T defaultValue) {
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
	public <T> T getAttribute(AttributeKey key) {
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
	public <T> T putAttribute(AttributeKey key, T value) {
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
	public <T> T removeAttribute(AttributeKey key) {
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
	public <T> T removeAttribute(AttributeKey key, T defaultValue) {
		T value = (T) attributes.remove(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
}
package org.redrune.game.entity.object;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.loaders.ObjectDefinitions;
import org.redrune.game.entity.Entity;
import org.redrune.game.global.WorldTile;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents an object that exists in the rs2 game world
 *
 * @author Matrix Team
 * @author Tyluur <itstyluur@gmail.com>
 */
@SuppressWarnings("serial")
public class WorldObject extends WorldTile implements Entity {
	
	/**
	 * The id of the object
	 */
	@Getter
	@Setter
	private int id;
	
	/**
	 * The type of the object
	 */
	@Getter
	@Setter
	private int type;
	
	/**
	 * The rotation of the object
	 */
	@Getter
	@Setter
	private int rotation;
	
	/**
	 * The life of the object, only used for trees/ore
	 */
	@Getter
	@Setter
	private int life;
	
	/**
	 * If the object is spawned
	 */
	@Getter
	@Setter
	private boolean spawned;
	
	/**
	 * The object that this object replaced when spawned
	 */
	@Getter
	@Setter
	private WorldObject replaced;
	
	/**
	 * The temporary attributes of this actor
	 */
	private transient ConcurrentHashMap<Object, Object> temporaryAttributes = new ConcurrentHashMap<>();
	
	public WorldObject(int id, int type, int rotation, WorldTile tile) {
		super(tile.getX(), tile.getY(), tile.getPlane());
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}
	
	public WorldObject(int id, int type, int rotation, int x, int y, int plane) {
		super(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}
	
	public WorldObject(int id, int type, int rotation, int x, int y, int plane, int life) {
		super(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = life;
	}
	
	@Override
	public WorldObject toObject() {
		return this;
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof WorldObject)) {
			return false;
		}
		WorldObject o = (WorldObject) object;
		return o.getId() == id && o.getWorldTile().matches(this) && o.getType() == type && o.rotation == rotation;
	}
	
	@Override
	public String toString() {
		return "WorldObject{id=" + id + ", name=" + getDefinitions().getName() + ", type=" + type + ", rotation=" + rotation + ", tile=" + getWorldTile() + "}";
	}
	
	public WorldTile getCentreLocation() {
		return new WorldTile(getX() + (getDefinitions().getSizeX() / 2), getY() + (getDefinitions().getSizeY() / 2), getPlane());
	}
	
	public WorldTile getFaceLocation() {
		ObjectDefinitions definitions = getDefinitions();
		return new WorldTile(getCoordFaceX(definitions.getSizeX(), definitions.getSizeY(), getRotation()), getCoordFaceY(definitions.getSizeX(), definitions.getSizeY(), getRotation()), getPlane());
	}
	
	/**
	 * Gets a transformed object of this object.
	 *
	 * @param id
	 * 		The new object id.
	 * @return The constructed game object.
	 */
	public WorldObject transform(int id) {
		return transform(id, rotation);
	}
	
	/**
	 * Gets a transformed object of this object.
	 *
	 * @param id
	 * 		The new object id.
	 * @param rotation
	 * 		The new rotation.
	 * @return The constructed game object.
	 */
	public WorldObject transform(int id, int rotation) {
		return new WorldObject(id, type, rotation, getWorldTile());
	}
	
	/**
	 * Gets a transformed object of this object.
	 *
	 * @param id
	 * 		The new object id.
	 * @param rotation
	 * 		The new rotation.
	 * @param tile
	 * 		The new location.
	 * @return The constructed game object.
	 */
	public WorldObject transform(int id, int rotation, WorldTile tile) {
		return new WorldObject(id, type, rotation, tile);
	}
	
	/**
	 * Gets a transformed object of this object.
	 *
	 * @param id
	 * 		The new object id.
	 * @param rotation
	 * 		The new rotation.
	 * @param type
	 * 		The object type.
	 * @return The constructed game object.
	 */
	public WorldObject transform(int id, int rotation, int type) {
		return new WorldObject(id, type, rotation, getWorldTile());
	}
	
	public void decrementObjectLife() {
		this.life--;
	}
	
	public ObjectDefinitions getDefinitions() {
		return ObjectDefinitions.getObjectDefinitions(id);
	}
	
	/**
	 * Puts the key into the attributes map
	 *
	 * @param key
	 * 		The key
	 * @param value
	 * 		The value
	 */
	public <K> K putTemporaryAttribute(Object key, K value) {
		temporaryAttributes.put(key, value);
		return value;
	}
	@SuppressWarnings("unchecked")
	public <K> K getTemporaryAttribute(Object key, K defaultValue) {
		K value = (K) temporaryAttributes.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public <K> K getTemporaryAttribute(Object key) {
		return (K) temporaryAttributes.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <K> K removeTemporaryAttribute(Object key) {
		return (K) temporaryAttributes.remove(key);
	}
	
	@SuppressWarnings("unchecked")
	public <K> K removeTemporaryAttribute(Object key, K defaultValue) {
		K value = (K) temporaryAttributes.remove(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
}

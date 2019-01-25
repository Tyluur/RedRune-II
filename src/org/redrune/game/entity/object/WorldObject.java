package org.redrune.game.entity.object;

import org.redrune.cache.loaders.ObjectDefinitions;
import org.redrune.game.entity.Entity;
import org.redrune.game.global.WorldTile;
import lombok.Getter;
import lombok.Setter;

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
		return "WorldObject{" + "id=" + id + ", type=" + type + ", rotation=" + rotation + ", life=" + life + '}';
	}
	
	public void decrementObjectLife() {
		this.life--;
	}
	
	public ObjectDefinitions getDefinitions() {
		return ObjectDefinitions.getObjectDefinitions(id);
	}
}

package org.redrune.game.node.object;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.ObjectDefinitionParser;
import org.redrune.cache.parse.definition.ObjectDefinition;
import org.redrune.game.node.Location;
import org.redrune.game.node.Node;

import java.util.Arrays;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class GameObject extends Node {
	
	/**
	 * The id of the object
	 */
	@Getter
	private final int id;
	
	/**
	 * The type of object this is
	 */
	@Getter
	private final int type;
	
	/**
	 * The rotation of the object
	 */
	@Getter
	private final int rotation;
	
	/**
	 * The object's definition.
	 */
	@Setter
	private ObjectDefinition definitions;
	
	/**
	 * Constructs a regular object with a 0 rotation and type 10.
	 *
	 * @param id
	 * 		The id of the object
	 * @param location
	 * 		The location of the object
	 */
	public GameObject(int id, Location location) {
		this(id, 10, 0, location);
	}
	
	/**
	 * Constructs an object
	 *
	 * @param id
	 * 		The id of the object
	 * @param type
	 * 		The type of the object
	 * @param rotation
	 * 		The rotation of the object
	 * @param location
	 * 		The location of the object
	 */
	public GameObject(int id, int type, int rotation, Location location) {
		super(location);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
	}
	
	@Override
	public void register() {
	
	}
	
	@Override
	public void deregister() {
	
	}
	
	@Override
	public int getSize() {
		return Math.max(getDefinitions().getSizeX(), getDefinitions().getSizeY());
	}
	
	/**
	 * Gets the definitions of the object
	 */
	public ObjectDefinition getDefinitions() {
		if (definitions == null) {
			setDefinitions(ObjectDefinitionParser.forId(id));
		}
		return definitions;
	}
	
	@Override
	public GameObject toGameObject() {
		return this;
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ", name=" + getDefinitions().getName() + ", options=" + Arrays.toString(getDefinitions().getOptions()) + ", type=" + type + ", rotation=" + rotation + "]";
	}
}
package org.redrune.rs2.node;

import lombok.Getter;
import lombok.Setter;
import org.redrune.rs2.world.map.Location;

/**
 * This is the parent class of all game nodes. Nodes are anything in the game which undergoes
 * registration/de-registration and is interactible.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public abstract class Node {
	
	/**
	 * Handles the registration of the node
	 */
	public abstract void register();
	
	/**
	 * Handles the de-registration of the node
	 */
	public abstract void deregister();
	
	/**
	 * Gets the size of the node
	 *
	 * @return The size of the node
	 */
	public abstract int getSize();
	
	/**
	 * The location of the node
	 */
	@Getter
	@Setter
	private Location location;
	
	/**
	 * If the entity has been renderable
	 */
	@Getter
	@Setter
	private transient boolean renderable;
	
	/**
	 * The construction of a new {@code Node} instance
	 *
	 * @param location
	 * 		The location of the node.
	 */
	protected Node(Location location) {
		this.location = location;
	}
}
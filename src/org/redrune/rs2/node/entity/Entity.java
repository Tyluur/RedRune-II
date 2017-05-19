package org.redrune.rs2.node.entity;

import lombok.Getter;
import lombok.Setter;
import org.redrune.rs2.node.Node;
import org.redrune.rs2.world.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public abstract class Entity extends Node {
	
	/**
	 * If the entity has been created
	 */
	@Getter
	@Setter
	private transient boolean created;
	
	@Getter
	@Setter
	private transient int index;
	
	protected Entity(Location location) {
		super(location);
	}
}

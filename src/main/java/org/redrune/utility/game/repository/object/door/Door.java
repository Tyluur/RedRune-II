package org.redrune.utility.game.repository.object.door;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a door.
 *
 * @author Emperor
 */
public class Door {
	
	/**
	 * The door's object id.
	 */
	@Getter
	private final int id;
	
	/**
	 * The door's replace object id.
	 */
	@Getter
	@Setter
	private int replaceId;
	
	/**
	 * If the player should automaticly walk through it.
	 */
	@Getter
	@Setter
	private boolean autoWalk;
	
	/**
	 * Constructs a new {@code DoorManager} {@code Object}.
	 */
	public Door(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Door{" + "id=" + id + ", replaceId=" + replaceId + ", autoWalk=" + autoWalk + '}';
	}
}

package org.redrune.utility.game.repository.object.climbable;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-21
 */
public class ClimbableObject {
	
	/**
	 * The id of the object
	 */
	@Getter
	private final int objectId;
	
	/**
	 * The name of the object
	 */
	@Getter
	private final String name;
	
	public ClimbableObject(int objectId, String name) {
		this.name = name;
		this.objectId = objectId;
	}
}

package org.redrune.utility.rs;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public enum FaceDirection {
	
	NORTH(0),
	NORTHEAST(1),
	EAST(2),
	SOUTHEAST(3),
	SOUTH(4),
	SOUTHWEST(5),
	WEST(6),
	NORTHWEST(7);
	
	@Getter
	private final int value;
	
	FaceDirection(int value) {
		this.value = value;
	}
	
	/**
	 * Gets the direction from the text
	 *
	 * @param text
	 * 		The text value of the direction
	 */
	public static FaceDirection getDirection(String text) {
		for (FaceDirection d : values()) {
			if (d.name().equalsIgnoreCase(text)) {
				return d;
			}
		}
		return null;
	}
}

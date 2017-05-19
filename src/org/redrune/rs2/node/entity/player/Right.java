package org.redrune.rs2.node.entity.player;

import lombok.Getter;

/**
 * The rights the player can have
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public enum Right {
	
	OWNER(2),
	ADMINISTRATOR(2),
	MODERATOR(1),
	EXTREME_DONATOR,
	DONATOR,
	PLAYER;
	
	/**
	 * The rights the player has in the client
	 */
	@Getter
	private final int clientRight;
	
	Right(int clientRight) {
		this.clientRight = clientRight;
	}
	
	Right() {
		this.clientRight = 0;
	}
	
}

package org.redrune.game.node.entity.player.data;

import lombok.Getter;

/**
 * The rights the player can have
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public enum PlayerRight {
	
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
	
	PlayerRight(int clientRight) {
		this.clientRight = clientRight;
	}
	
	PlayerRight() {
		this.clientRight = 0;
	}
	
}
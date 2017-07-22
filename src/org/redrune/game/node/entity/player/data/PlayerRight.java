package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import org.redrune.game.node.entity.player.Player;

import java.util.Arrays;
import java.util.Optional;

/**
 * The rights the player can have
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public enum PlayerRight {
	
	OWNER(2),
	ADMINISTRATOR(2) {
		@Override
		public boolean playerHasRights(Player player) {
			return player.getDetails().rightsContains(OWNER, ADMINISTRATOR);
		}
	},
	MODERATOR(1) {
		@Override
		public boolean playerHasRights(Player player) {
			return player.getDetails().rightsContains(OWNER, ADMINISTRATOR, MODERATOR);
		}
	},
	EXTREME_DONATOR,
	DONATOR,
	PLAYER;
	
	/**
	 * The rights the player has in the client
	 */
	@Getter
	private final byte clientRight;
	
	PlayerRight(int clientRight) {
		this.clientRight = (byte) clientRight;
	}
	
	PlayerRight() {
		this.clientRight = 0;
	}
	
	/**
	 * Finds the right optional by the {@link PlayerRight#name}.
	 *
	 * @param name
	 * 		The name to look for.
	 */
	public static Optional<PlayerRight> playerRightOptional(String name) {
		return Arrays.stream(values()).filter(right -> right.name().equalsIgnoreCase(name)).findFirst();
	}
	
	/**
	 * Checking that the player has access to this right
	 *
	 * @param player
	 * 		The player
	 */
	public boolean playerHasRights(Player player) {
		return player.getDetails().rightsContains(this);
	}
	
}
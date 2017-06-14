package org.redrune.core.master;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class MasterPlayer {
	
	/**
	 * The uid of the player's session
	 */
	@Getter
	private final long uid;
	
	/**
	 * The username of the player
	 */
	@Getter
	private final String username;
	
	/**
	 * The id of the world the player is in
	 */
	@Getter
	private final int worldId;
	
	public MasterPlayer(long uid, String username, int worldId) {
		this.uid = uid;
		this.username = username;
		this.worldId = worldId;
	}
	
	@Override
	public String toString() {
		return "MasterPlayer{" + "uid=" + uid + ", username='" + username + '\'' + '}';
	}
}

package org.redrune.core.master;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class MasterPlayer {
	
	/**
	 * The uid of the player
	 */
	@Getter
	private final long uid;
	
	/**
	 * The username of the player
	 */
	@Getter
	private final String username;
	
	public MasterPlayer(long uid, String username) {
		this.uid = uid;
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "MasterPlayer{" + "uid=" + uid + ", username='" + username + '\'' + '}';
	}
}

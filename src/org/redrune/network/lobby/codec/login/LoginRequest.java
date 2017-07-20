package org.redrune.network.lobby.codec.login;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public enum LoginRequest {
	
	/**
	 * The preparation request stage
	 */
	PRE_STAGE,
	
	/**
	 * The entering lobby stage
	 */
	LOBBY_ENTRANCE,
	
	/**
	 * The world login request stage
	 */
	WORLD_ENTRANCE
}
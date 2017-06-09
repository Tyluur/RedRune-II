package org.redrune.network.rs666.codec.login;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public enum LoginState {
	
	/**
	 * The preparing for login state.
	 */
	PRE_STAGE,
	
	/**
	 * The entering lobby state.
	 */
	LOBBY_ENTRANCE,
	
	/**
	 * The logging in state.
	 */
	GAME_ENTRANCE,
}

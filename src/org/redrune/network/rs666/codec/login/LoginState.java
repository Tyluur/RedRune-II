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
	LOBBY_FINALIZATION,
	
	/**
	 * The logging in state.
	 */
	LOGIN_FINALIZATION,
}

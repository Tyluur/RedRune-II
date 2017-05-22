package org.redrune.network.protocol.login.msg;


import org.redrune.network.protocol.login.LoginReadEvent.LoginRequest;
import org.redrune.rs2.node.entity.player.components.managers.InterfaceManager.DisplayMode;
import org.redrune.utility.isaac.IsaacRandomPair;

import lombok.Getter;

/**
 * LoginRequestEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class LoginRequestEvent {

	@Getter
	private final LoginRequest loginRequest;

	@Getter
	private final int major;

	@Getter
	private final int minor;

	@Getter
	private final String username;

	@Getter
	private final String password;

	@Getter
	private final IsaacRandomPair isaacPair;

	@Getter
	private final DisplayMode displayMode;

	public LoginRequestEvent(LoginRequest loginRequest, int major, int minor, String username, String password,
			IsaacRandomPair isaacPair, DisplayMode displayMode) {
		this.loginRequest = loginRequest;
		this.major = major;
		this.minor = minor;
		this.username = username;
		this.password = password;
		this.isaacPair = isaacPair;
		this.displayMode = displayMode;
	}

}

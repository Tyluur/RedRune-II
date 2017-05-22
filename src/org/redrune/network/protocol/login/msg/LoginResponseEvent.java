package org.redrune.network.protocol.login.msg;

import org.redrune.network.protocol.ProtocolResponse;
import org.redrune.network.protocol.login.LoginReadEvent.LoginRequest;
import org.redrune.rs2.node.entity.player.Player;

public class LoginResponseEvent {

	private final LoginRequest loginRequest;

	private final ProtocolResponse response;

	private final Player player;

	public LoginResponseEvent(LoginRequest loginRequest, ProtocolResponse response, Player player) {
		this.loginRequest = loginRequest;
		this.response = response;
		this.player = player;
	}

	public LoginRequest getLoginRequest() {
		return loginRequest;
	}

	public ProtocolResponse getResponse() {
		return response;
	}

	public Player getPlayer() {
		return player;
	}

}

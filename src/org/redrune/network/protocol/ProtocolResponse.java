package org.redrune.network.protocol;

public enum ProtocolResponse {

	SUCCESSFUL_CONNECTION(0),

	PEND_2000MS(1),

	SUCCESSFUL_LOGIN(2),

	INVALID_USERNAME_OR_PASSWORD(3),

	DISABLED_ACCOUNT(4),

	LOGGED_IN(5),

	OUT_OF_DATE(6),

	WORLD_FULL(7),

	LOGINSERVER_OFFLINE(8),

	LOGIN_LIMIT_EXCEEDED(9),

	BAD_SESSION(10),

	LOGINSERVER_REJECTED(11),

	MEMBERS_REQUIRED(12),

	INCOMPLETE_LOGIN(13),

	SERVER_UPDATING(14),

	LOGIN_ATTEMPTS_EXCEEDED(16),

	INSIDE_MEMBERS_AREA(17);

	private final int clientId;

	ProtocolResponse(int clientId) {
		this.clientId = clientId;
	}

	public int getClientId() {
		return clientId;
	}

}

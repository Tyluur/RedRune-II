package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class SendPrivateMessagePacket implements PacketContext {

	private final String username;

	private final String message;

	public SendPrivateMessagePacket(String username, String message) {
		this.username = username;
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public String getMessage() {
		return message;
	}

}

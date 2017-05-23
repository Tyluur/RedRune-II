package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;
import org.redrune.rs2.node.entity.player.components.PlayerRight;

/**
 * ReceivePrivateMessagePacket.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class ReceivePrivateMessagePacket implements PacketContext {

	private final String username;

	private final String message;

	private final PlayerRight rights;

	public ReceivePrivateMessagePacket(String username, String message, PlayerRight rights) {
		this.username = username;
		this.message = message;
		this.rights = rights;
	}

	public String getUsername() {
		return username;
	}

	public String getMessage() {
		return message;
	}

	public PlayerRight getRights() {
		return rights;
	}

}

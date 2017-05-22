package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;
import org.redrune.rs2.node.entity.player.components.PlayerRight;

public class ChatPacket implements PacketEvent {

	private final String message;

	private final PlayerRight rights;

	private final int index;

	private final int effect;

	public ChatPacket(String message, PlayerRight rights, int index, int effect) {
		this.message = message;
		this.rights = rights;
		this.index = index;
		this.effect = effect;
	}

	public String getMessage() {
		return message;
	}

	public PlayerRight getRights() {
		return rights;
	}

	public int getIndex() {
		return index;
	}

	public int getEffect() {
		return effect;
	}

}

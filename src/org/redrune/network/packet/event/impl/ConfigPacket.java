package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class ConfigPacket implements PacketContext {

	private final int id;

	private final int value;

	public ConfigPacket(int id, int value) {
		this.id = id;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public int getValue() {
		return value;
	}

}

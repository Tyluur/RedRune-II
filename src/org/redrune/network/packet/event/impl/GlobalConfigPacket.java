package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

public class GlobalConfigPacket implements PacketEvent {

	private final int id;

	private final int value;

	public GlobalConfigPacket(int id, int value) {
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

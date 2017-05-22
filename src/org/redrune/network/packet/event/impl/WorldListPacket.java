package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

public class WorldListPacket implements PacketEvent {

	private final boolean update;

	public WorldListPacket(boolean update) {
		this.update = update;
	}

	public boolean isUpdate() {
		return update;
	}

}

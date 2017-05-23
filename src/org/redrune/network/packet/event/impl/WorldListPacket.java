package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class WorldListPacket implements PacketContext {

	private final boolean update;

	public WorldListPacket(boolean update) {
		this.update = update;
	}

	public boolean isUpdate() {
		return update;
	}

}

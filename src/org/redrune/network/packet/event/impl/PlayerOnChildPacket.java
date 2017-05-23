package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class PlayerOnChildPacket implements PacketContext {

	private final int interfaceId;

	private final int childId;

	public PlayerOnChildPacket(int interfaceId, int childId) {
		this.interfaceId = interfaceId;
		this.childId = childId;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getChildId() {
		return childId;
	}

}

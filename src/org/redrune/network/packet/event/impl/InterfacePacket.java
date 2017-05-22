package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

public class InterfacePacket implements PacketEvent {

	private final int paneId;

	private final int interfaceId;

	private final int childId;

	private final boolean walkable;

	public InterfacePacket(int paneId, int interfaceId, int childId, boolean walkable) {
		this.paneId = paneId;
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.walkable = walkable;
	}

	public int getPaneId() {
		return paneId;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getChildId() {
		return childId;
	}

	public boolean isWalkable() {
		return walkable;
	}

}

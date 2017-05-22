package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

public class CloseInterfacePacket implements PacketEvent {

	private final int windowId;

	private final int childId;

	public CloseInterfacePacket(int windowId, int childId) {
		this.windowId = windowId;
		this.childId = childId;
	}

	public int getWindowId() {
		return windowId;
	}

	public int getChildId() {
		return childId;
	}

}

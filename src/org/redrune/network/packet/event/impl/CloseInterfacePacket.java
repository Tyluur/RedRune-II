package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class CloseInterfacePacket implements PacketContext {

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

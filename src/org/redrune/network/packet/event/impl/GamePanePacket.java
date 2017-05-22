package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

public class GamePanePacket implements PacketEvent {

	private final int paneId;

	private final int paneType;

	public GamePanePacket(int paneId, int paneType) {
		this.paneId = paneId;
		this.paneType = paneType;
	}

	public int getPaneId() {
		return paneId;
	}

	public int getPaneType() {
		return paneType;
	}

}

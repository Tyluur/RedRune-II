package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

public class StringOnChildPacket implements PacketEvent {

	private final int interfaceId;

	private final int childId;

	private final String string;

	public StringOnChildPacket(int interfaceId, int childId, String string) {
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.string = string;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getChildId() {
		return childId;
	}

	public String getString() {
		return string;
	}

}

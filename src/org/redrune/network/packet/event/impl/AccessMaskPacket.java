package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

public class AccessMaskPacket implements PacketEvent {

	private final int interfaceId;

	private final int childId;

	private final int startingSlot;

	private final int finishingSlot;

	private final int hash;

	public AccessMaskPacket(int interfaceId, int childId, int startingSlot, int finishingSlot, int hash) {
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.startingSlot = startingSlot;
		this.finishingSlot = finishingSlot;
		this.hash = hash;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getChildId() {
		return childId;
	}

	public int getStartingSlot() {
		return startingSlot;
	}

	public int getFinishingSlot() {
		return finishingSlot;
	}

	public int getHash() {
		return hash;
	}

}

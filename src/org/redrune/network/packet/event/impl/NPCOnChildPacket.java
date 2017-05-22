package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

public class NPCOnChildPacket implements PacketEvent {

	private final int interfaceId;

	private final int childId;

	private final int npcId;

	public NPCOnChildPacket(int interfaceId, int childId, int npcId) {
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.npcId = npcId;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getChildId() {
		return childId;
	}

	public int getNpcId() {
		return npcId;
	}

}

package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

public class NPCInterfacePacket implements PacketEvent {

	private final int npcIndex;

	private final int paneId;

	private final int interfaceId;

	private final int childId;

	private final boolean walkable;

	public NPCInterfacePacket(int npcIndex, int paneId, int interfaceId, int childId, boolean walkable) {
		this.npcIndex = npcIndex;
		this.paneId = paneId;
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.walkable = walkable;
	}

	public int getNPCIndex() {
		return npcIndex;
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

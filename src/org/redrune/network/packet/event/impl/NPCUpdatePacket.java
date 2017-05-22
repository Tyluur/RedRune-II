package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;
import org.redrune.rs2.node.entity.player.Player;

public class NPCUpdatePacket implements PacketEvent {

	private final Player player;

	public NPCUpdatePacket(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

}

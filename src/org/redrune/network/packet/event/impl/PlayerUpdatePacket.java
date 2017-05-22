package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;
import org.redrune.rs2.node.entity.player.Player;

public class PlayerUpdatePacket implements PacketEvent {

	private final Player player;

	public PlayerUpdatePacket(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

}

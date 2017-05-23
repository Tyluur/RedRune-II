package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;
import org.redrune.rs2.node.entity.player.Player;

public class RunEnergyPacket implements PacketContext {

	private final Player player;

	public RunEnergyPacket(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

}

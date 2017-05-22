package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;
import org.redrune.rs2.node.entity.player.Player;

public class RegionPacket implements PacketEvent {

	private final Player player;

	private final boolean login;

	public RegionPacket(Player player, boolean login) {
		this.player = player;
		this.login = login;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isLogin() {
		return login;
	}

}

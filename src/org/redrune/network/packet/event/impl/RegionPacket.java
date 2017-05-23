package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;
import org.redrune.rs2.node.entity.player.Player;

public class RegionPacket implements PacketContext {

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

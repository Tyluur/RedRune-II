package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;
import org.redrune.rs2.node.entity.player.Player;

public class LogoutPacket implements PacketContext {

	private final Player player;

	private final boolean toLobby;

	public LogoutPacket(Player player, boolean toLobby) {
		this.player = player;
		this.toLobby = toLobby;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isToLobby() {
		return toLobby;
	}

}

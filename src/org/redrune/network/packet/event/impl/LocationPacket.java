package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.world.Location;

/**
 * LocationPacket.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class LocationPacket implements PacketEvent {

	private final Player player;

	private final Location location;

	public LocationPacket(Player player, Location location) {
		this.player = player;
		this.location = location;
	}

	public Player getPlayer() {
		return player;
	}

	public Location getLocation() {
		return location;
	}

}

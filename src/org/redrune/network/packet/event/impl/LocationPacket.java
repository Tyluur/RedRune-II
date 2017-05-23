package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.world.Location;

/**
 * LocationPacket.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class LocationPacket implements PacketContext {

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

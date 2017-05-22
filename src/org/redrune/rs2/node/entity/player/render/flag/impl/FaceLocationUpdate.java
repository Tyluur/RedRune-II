package org.redrune.rs2.node.entity.player.render.flag.impl;

import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.rs2.node.entity.Entity;
import org.redrune.rs2.node.entity.player.render.flag.UpdateFlag;
import org.redrune.rs2.world.Location;

/**
 * Represents the face location update mask.
 *
 * @author Emperor
 */
public class FaceLocationUpdate extends UpdateFlag {
	
	/**
	 * The location to face.
	 */
	private final Location location;
	
	/**
	 * If the entity is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * The entity's location.
	 */
	private final Location currentLocation;
	
	/**
	 * Constructs a new {@code FaceLocationUpdate} {@code Object}.
	 *
	 * @param entity
	 * 		The entity facing a location.
	 * @param location
	 * 		The location to face.
	 */
	public FaceLocationUpdate(Entity entity, Location location) {
		if (location != null) {
			this.location = location;
		} else {
			this.location = Location.locate(0, 0, 0);
		}
		this.npc = entity.isNPC();
		this.currentLocation = entity.getLocation();
	}
	
	@Override
	public void write(PacketBuilder bldr) {
		if (npc) {
			bldr.writeLEShortA(location.getX() << 1);
			bldr.writeLEShortA(location.getY() << 1);
		} else {
			final int dX = currentLocation.getX() - location.getX();
			final int dY = currentLocation.getY() - location.getY();
			bldr.writeLEShortA(((int) (Math.atan2(dX, dY) * 2607.5945876176133)) & 0x3fff);
		}
	}
	
	@Override
	public int getMaskData() {
		return 0x20;
	}
	
	@Override
	public int getOrdinal() {
		return npc ? 13 : 14;
	}
	
}
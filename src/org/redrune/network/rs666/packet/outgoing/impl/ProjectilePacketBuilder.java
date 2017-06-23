package org.redrune.network.rs666.packet.outgoing.impl;

import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.rs.Projectile;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public class ProjectilePacketBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The projectile we're creating
	 */
	private final Projectile projectile;
	
	public ProjectilePacketBuilder(Projectile projectile) {
		this.projectile = projectile;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(27);
		Location end = projectile.isLocationBased() ? projectile.getEndLocation() : projectile.getVictim().getLocation();
		Location start = projectile.getSourceLocation();
		player.getTransmitter().send(new TileLocationUpdate(start).build(player));
		int x = start.getX() - (start.getRegionX() << 3);
		int y = start.getY() - (start.getRegionY() << 3);
		bldr.writeByte((x & 0x7) << 3 | y & 0x7).writeByte(end.getX() - start.getX()).writeByte(end.getY() - start.getY());
		bldr.writeShort(projectile.getVictim() != null ? (projectile.getVictim().isPlayer() ? -(projectile.getVictim().getIndex() + 1) : (projectile.getVictim().getIndex() + 1)) : -1);
		bldr.writeShort(projectile.getProjectileId());
		bldr.writeByte(projectile.getStartHeight());
		bldr.writeByte(projectile.getEndHeight());
		bldr.writeShort(projectile.getDelay());
		bldr.writeShort(projectile.getSpeed());
		bldr.writeByte(projectile.getAngle());
		bldr.writeShort(projectile.getCreatorSize() * 64 + projectile.getStartDistanceOffset() * 64);
		return bldr.toPacket();
	}
}

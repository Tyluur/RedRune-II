package org.redrune.networking.codec.packet.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.route.RouteFinder;
import org.redrune.game.global.map.route.strategy.FixedTileStrategy;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class RoutefinderPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(WALKING_PACKET, MINI_WALKING_PACKET);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
		switch (packetId) {
			case WALKING_PACKET:
			case MINI_WALKING_PACKET: {
				if (!player.hasStarted() || !player.getAttributes().clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				long currentTime = Misc.currentTimeMillis();
				if (player.getLocks().isMovementLocked()) {
					return;
				}
				if (player.getFreezeDelay() >= currentTime) {
					player.getPackets().sendGameMessage("A magical force prevents you from moving.");
					return;
				}
				// the x to walk to
				int destX = stream.readUnsignedShortLE128();
				// the y to walk to
				int destY = stream.readUnsignedShortLE128();
				// if the player should force running
				boolean forceRun = stream.readByte() == 1;
				player.stopAll();
				// forces the new run flag
				if (forceRun) {
					player.setRunModeOn(true);
				}
				// calculates the amount of steps in the path
				int calculatedSteps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getPlane(), player.getSize(), new FixedTileStrategy(destX, destY), true);
				// the buffer with the x steps
				int[] bufferX = RouteFinder.getLastPathBufferX();
				// the buffer with they steps
				int[] bufferY = RouteFinder.getLastPathBufferY();
				
				// adds walk steps to the movement queue
				int last = -1;
				for (int i = calculatedSteps - 1; i >= 0; i--) {
					if (!player.addWalkSteps(bufferX[i], bufferY[i], 25, true)) {
						break;
					}
					last = i;
				}
				
				// sends destination on the minimap
				if (last != -1) {
					WorldTile tile = new WorldTile(bufferX[last], bufferY[last], player.getPlane());
					player.getPackets().sendMinimapFlag(tile.getLocalX(player.getLastLoadedMapRegionTile()), tile.getLocalY(player.getLastLoadedMapRegionTile()));
				} else {
					player.getPackets().sendResetMinimapFlag();
				}
			}
			break;
		}
	}
}

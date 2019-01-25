package org.redrune.networking.codec.packet.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class RegionLoadConfirmationPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(DONE_LOADING_REGION);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
		if (!player.clientHasLoadedMapRegion()) {
			player.setClientHasLoadedMapRegion();
		}
		player.getPacketSender().refreshSpawnedObjects();
		player.getPacketSender().refreshSpawnedItems();
	}
}

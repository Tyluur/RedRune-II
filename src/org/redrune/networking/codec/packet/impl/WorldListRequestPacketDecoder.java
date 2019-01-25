package org.redrune.networking.codec.packet.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class WorldListRequestPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(WORLD_LIST_REQUEST_PACKET);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
		int updateType = stream.readInt();
		player.getPackets().sendWorldList(updateType == 0);
	}
}

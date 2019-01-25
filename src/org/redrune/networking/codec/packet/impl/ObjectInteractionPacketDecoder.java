package org.redrune.networking.codec.packet.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.codec.decode.handlers.ObjectHandler;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;

import static org.redrune.utility.game.ClickOption.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class ObjectInteractionPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(OBJECT_CLICK1_PACKET, OBJECT_CLICK2_PACKET, OBJECT_CLICK3_PACKET, OBJECT_EXAMINE_PACKET, ITEM_ON_OBJECT_PACKET);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
		switch (packetId) {
			case OBJECT_CLICK1_PACKET:
				ObjectHandler.decodeObjectStream(player, stream, FIRST);
				break;
			case OBJECT_CLICK2_PACKET:
				ObjectHandler.decodeObjectStream(player, stream, SECOND);
				break;
			case OBJECT_CLICK3_PACKET:
				ObjectHandler.decodeObjectStream(player, stream, THIRD);
				break;
			case OBJECT_EXAMINE_PACKET:
				ObjectHandler.decodeObjectStream(player, stream, EXAMINE);
				break;
			case ITEM_ON_OBJECT_PACKET:
				ObjectHandler.handleItemOnObject(player, stream);
				break;
		}
	}
}

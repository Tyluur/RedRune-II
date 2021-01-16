package org.redrune.networking.packet.incoming.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.context.impl.CapeColorCustomizationPacketContext;
import org.redrune.networking.packet.incoming.IncomingPacketReader;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class CapeColorCustomizationPacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(COLOR_ID_PACKET);
	}
	
	@Override
	public PacketContext read(Player player, Packet packet) {
		int colorId = packet.readUnsignedShort();
		return new CapeColorCustomizationPacketContext(colorId);
	}
}

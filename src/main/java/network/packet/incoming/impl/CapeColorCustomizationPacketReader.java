package network.packet.incoming.impl;

import game.entity.actor.player.Player;
import network.packet.Packet;
import network.packet.context.PacketContext;
import network.packet.context.impl.CapeColorCustomizationPacketContext;
import network.packet.incoming.IncomingPacketReader;

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

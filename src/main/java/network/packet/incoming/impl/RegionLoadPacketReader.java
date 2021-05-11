package network.packet.incoming.impl;

import game.entity.actor.player.Player;
import network.packet.Packet;
import network.packet.context.PacketContext;
import network.packet.context.impl.RegionLoadPacketContext;
import network.packet.incoming.IncomingPacketReader;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class RegionLoadPacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(DONE_LOADING_REGION);
	}
	
	@Override
	public PacketContext read(Player player, Packet packet) {
		return new RegionLoadPacketContext();
	}
}

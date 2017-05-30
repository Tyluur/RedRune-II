package org.redrune.network.rs666.packet.structure.out;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class MinimapFlagBuilder implements OutgoingPacketStructure {
	
	/**
	 * The map x type
	 */
	private final int mapX;
	
	/**
	 * The map y type
	 */
	private final int mapY;
	
	public MinimapFlagBuilder(int mapX, int mapY) {
		this.mapX = mapX;
		this.mapY = mapY;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(16);
		bldr.writeByteC(mapX);
		bldr.writeByteC(mapY);
		return bldr.toPacket();
	}
}

package org.redrune.network.rs666.packet.structure.out;

import org.redrune.anetworking.rs666.packet.PacketBuilder;
import org.redrune.anetworking.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class GameWindowBuilder implements OutgoingPacketStructure {
	
	/**
	 * The pane id.
	 */
	private final int paneId;
	
	/**
	 * The sub-window id.
	 */
	private final int subWindowId;
	
	public GameWindowBuilder(int paneId, int subWindowId) {
		this.paneId = paneId;
		this.subWindowId = subWindowId;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(100);
		bldr.writeByteA(subWindowId);
		bldr.writeLEShortA(paneId);
		return bldr.toPacket();
	}
}

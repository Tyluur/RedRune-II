package org.redrune.network.rs666.packet.structure.in;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.network.rs666.packet.structure.out.PublicChatBuilder;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.world.World;
import org.redrune.utility.Misc;
import org.redrune.utility.io.BufferUtils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class CommunicationsPacketStructure implements IncomingPacketStructure {
	
	/**
	 * The public chat message opcode.
	 */
	private static final int PUBLIC_CHAT = 19;
	
	@Override
	public int[] bindings() {
		return Misc.arguments(19);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		switch (packet.getOpcode()) {
			case PUBLIC_CHAT:
				readPublicChatPacket(player, packet);
				break;
		}
	}
	
	/**
	 * Reads the packet for a public chat message
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	private void readPublicChatPacket(Player player, Packet packet) {
		int effects = packet.readShort();
		int length = packet.readByte() & 0xFF;
		if (length < 0) {
			return;
		}
		String text = Misc.optimizeText(BufferUtils.decompressHuffman(packet, length));
		for (Player p : World.get().getPlayers()) {
			if (p == null || p.getLocation().getRegionId() != player.getLocation().getRegionId()) {
				continue;
			}
			p.getNetworkSession().write(new PublicChatBuilder(player.getIndex(), player.getDetails().getDominantRight().getClientRight(), text, effects).build(p));
		}
	}
}

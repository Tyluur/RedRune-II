package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.context.CommandEventContext;
import org.redrune.game.node.entity.player.event.impl.CommandEvent;
import org.redrune.game.world.World;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;
import org.redrune.network.rs666.packet.outgoing.impl.PublicChatBuilder;
import org.redrune.utility.BufferUtils;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class CommunicationsPacketDecoder implements IncomingPacketDecoder {
	
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
		if (text.startsWith("::")) {
			player.getManager().getEvents().executeEvent(player, new CommandEvent(new CommandEventContext(text.replaceFirst("::", "").split(" "), false)));
			return;
		}
		for (Player p : World.get().getPlayers()) {
			if (p == null || p.getLocation().getRegionId() != player.getLocation().getRegionId()) {
				continue;
			}
			p.getNetworkSession().write(new PublicChatBuilder(player.getIndex(), player.getDetails().getDominantRight().getClientRight(), text, effects).build(p));
		}
	}
}

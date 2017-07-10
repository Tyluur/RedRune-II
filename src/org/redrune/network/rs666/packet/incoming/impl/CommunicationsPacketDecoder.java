package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.content.event.context.CommandEventContext;
import org.redrune.game.content.event.impl.CommandEvent;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;
import org.redrune.network.rs666.packet.outgoing.impl.PublicChatBuilder;
import org.redrune.game.content.event.EventRepository;
import org.redrune.utility.tool.BufferUtils;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class CommunicationsPacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The public chat message opcode.
	 */
	private static final int PUBLIC_CHAT = 19;
	
	/**
	 * The private chat message opcode
	 */
	private static final int PRIVATE_MESSAGE = 13;
	
	/**
	 * The  opcode of the game bar settings flag
	 */
	private static final int BAR_SETTINGS = 78;
	
	@Override
	public int[] bindings() {
		return arguments(PUBLIC_CHAT, PRIVATE_MESSAGE, BAR_SETTINGS);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		switch (packet.getOpcode()) {
			case PUBLIC_CHAT:
				readPublicChatPacket(player, packet);
				break;
			case PRIVATE_MESSAGE:
				readPrivateMessagePacket(player, packet);
				break;
			case BAR_SETTINGS:
				readGameBarPacket(player, packet);
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
			EventRepository.executeEvent(player, CommandEvent.class, new CommandEventContext(text.replaceFirst("::", "").split(" "), false));
			return;
		}
		for (Player p : World.get().getPlayers()) {
			if (p == null || p.getLocation().getRegionId() != player.getLocation().getRegionId()) {
				continue;
			}
			p.getNetworkSession().write(new PublicChatBuilder(player.getIndex(), player.getDetails().getDominantRight().getClientRight(), text, effects).build(p));
		}
	}
	
	/**
	 * Decodes the packet saying that we should send a private message
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	private void readPrivateMessagePacket(Player player, Packet packet) {
		String name = packet.readRS2String();
		byte length = packet.readByte();
		String message = BufferUtils.decompressHuffman(packet, length);
		
		//TODO: RS2MasterCommunication.writeMasterPacket(new ClientPrivateMessageBuilder(new ClientPrivateMessageContext(player.getNetworkSession().getUid(), player.getDetails().getUsername(), player.getDetails().getDominantRight().getClientRight(), name, message)).build());
	}
	
	/**
	 * Reads the packet for the game bar settings.
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	private void readGameBarPacket(Player player, Packet packet) {
		int publicFlag = packet.readByte();
		int privateFlag = packet.readByte();
		int friendsFlag = packet.readByte();
		
		
		
		System.out.println(publicFlag + ", " + privateFlag + ", " + friendsFlag);
	}
}

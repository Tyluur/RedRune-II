package org.redrune.net.packet.incoming.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.net.packet.Packet;
import org.redrune.net.packet.context.PacketContext;
import org.redrune.net.packet.context.impl.CommandPacketContext;
import org.redrune.net.packet.incoming.IncomingPacketReader;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class CommandPacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(COMMANDS_PACKET);
	}
	
	@Override
	public PacketContext read(Player player, Packet packet) {
		boolean clientCommand = packet.readUnsignedByte() == 1;
		@SuppressWarnings("unused") boolean unknown = packet.readUnsignedByte() == 1;
		String command = packet.readRS2String();
		return new CommandPacketContext(clientCommand, unknown, command);
	}
}

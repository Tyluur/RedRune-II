package org.redrune.networking.codec.packet.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.PluginRepository;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class CommandPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(COMMANDS_PACKET);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
		if (!player.isRunning()) {
			return;
		}
		boolean clientCommand = stream.readUnsignedByte() == 1;
		@SuppressWarnings("unused") boolean unknown = stream.readUnsignedByte() == 1;
		String command = stream.readString();
		PluginRepository.handleCommand(player, command.replaceFirst("::", "").split(" "), true, clientCommand);
	}
}

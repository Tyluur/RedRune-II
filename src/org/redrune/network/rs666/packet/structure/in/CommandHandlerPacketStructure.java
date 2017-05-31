package org.redrune.network.rs666.packet.structure.in;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.context.CommandEventContext;
import org.redrune.game.node.entity.player.event.impl.CommandEvent;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public class CommandHandlerPacketStructure implements IncomingPacketStructure {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(12);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		if (packet.getBuffer().readableBytes() < 1) {
			return;
		}
		packet.readUnsignedByte();
		packet.readUnsignedByte();
		String command = packet.readRS2String();
		String[] args = command.toLowerCase().split(" ");
		player.getManager().getEvents().addEvent(new CommandEvent(new CommandEventContext(args)));
	}
}

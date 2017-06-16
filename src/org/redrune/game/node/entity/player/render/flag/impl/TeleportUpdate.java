package org.redrune.game.node.entity.player.render.flag.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.UpdateFlag;
import org.redrune.network.rs666.packet.PacketBuilder;

/**
 * Represents an animation update flag.
 *
 * @author Emperor
 */
public class TeleportUpdate extends UpdateFlag {
	
	@Override
	public void write(Player outgoing, PacketBuilder packet) {
		packet.writeByteC(127);
	}
	
	@Override
	public int getMaskData() {
		return 0x400;
	}
	
	@Override
	public int getOrdinal() {
		return 11;
	}
	
}

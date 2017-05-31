package org.redrune.game.node.entity.player.render.flag.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.UpdateFlag;
import org.redrune.network.rs666.packet.PacketBuilder;

/**
 * Represents the movement update flag.
 *
 * @author Emperor
 */
public class MovementUpdate extends UpdateFlag {
	
	/**
	 * The speed to move.
	 */
	private final int type;
	
	/**
	 * Constructs a new {@code MovementUpdate} {@code Object}.
	 *
	 * @param player
	 * 		The player.
	 */
	public MovementUpdate(Player player) {
		if (player.getWalkingQueue().getRunDir() != -1) {
			type = 2;
		} else {
			type = 1;
		}
	}
	
	@Override
	public void write(PacketBuilder outgoing) {
		outgoing.writeByteS(type);
	}
	
	@Override
	public int getMaskData() {
		return 0x40;
	}
	
	@Override
	public int getOrdinal() {
		return 17;
	}
	
}
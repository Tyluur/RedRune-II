package org.redrune.networking.packet.context.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-04
 */
public class ClientDetailsPacketContext extends PacketContext {
	
	/**
	 * The count variable, purpose unknown
	 */
	private final int count;
	
	public ClientDetailsPacketContext(int count) {
		this.count = count;
	}
	
	@Override
	public void handle(Player player) {
	
	}
}

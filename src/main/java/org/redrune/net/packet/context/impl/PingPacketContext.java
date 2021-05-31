package org.redrune.net.packet.context.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.net.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class PingPacketContext extends PacketContext {
	
	/**
	 * The latency between client and server
	 */
	private final int ping;
	
	public PingPacketContext(int ping) {
		this.ping = ping;
	}
	
	@Override
	public void handle(Player player) {
	
	}
}

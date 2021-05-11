package network.packet.context.impl;

import game.entity.actor.player.Player;
import network.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@icloud.com>
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

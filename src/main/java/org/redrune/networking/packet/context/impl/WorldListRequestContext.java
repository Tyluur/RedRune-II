package org.redrune.networking.packet.context.impl;


import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.outgoing.impl.WorldListPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-04
 */
public class WorldListRequestContext extends PacketContext {
	
	/**
	 * The type of update that is being requested
	 */

	private final int updateType;
	
	public WorldListRequestContext(int updateType) {
		this.updateType = updateType;
	}
	
	@Override
	public void handle(Player player) {
		player.getSession().write(new WorldListPacketBuilder(updateType == 0));
	}
}

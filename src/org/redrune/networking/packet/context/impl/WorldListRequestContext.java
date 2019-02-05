package org.redrune.networking.packet.context.impl;

import lombok.Getter;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.bldr.outgoing.impl.WorldListPacketBuilder;
import org.redrune.networking.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-04
 */
public class WorldListRequestContext extends PacketContext {
	
	/**
	 * The type of update that is being requested
	 */
	@Getter
	private final int updateType;
	
	public WorldListRequestContext(int updateType) {
		this.updateType = updateType;
	}
	
	@Override
	public void handle(Player player) {
		player.getSession().write(new WorldListPacketBuilder(updateType == 0));
	}
}

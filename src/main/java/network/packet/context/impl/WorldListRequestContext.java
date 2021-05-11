package network.packet.context.impl;


import game.entity.actor.player.Player;
import network.packet.context.PacketContext;
import network.packet.outgoing.impl.WorldListPacketBuilder;

/**
 * @author Tyluur <itstyluur@icloud.com>
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

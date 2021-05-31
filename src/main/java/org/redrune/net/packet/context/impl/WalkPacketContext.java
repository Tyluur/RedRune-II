package org.redrune.net.packet.context.impl;

import org.redrune.game.content.entity.actor.player.event.player.PlayerWalkEvent;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.net.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class WalkPacketContext extends PacketContext {
	
	/**
	 * The destination x to travel to
	 */
	private final int destX;
	
	/**
	 * The destination y to travel to
	 */
	private final int destY;
	
	/**
	 * If the player should be forced to run
	 */
	private final boolean forceRun;
	
	public WalkPacketContext(int destX, int destY, boolean forceRun) {
		this.destX = destX;
		this.destY = destY;
		this.forceRun = forceRun;
	}
	
	@Override
	public void handle(Player player) {
		player.getEventManager().start(new PlayerWalkEvent(destX, destY, forceRun));
	}
}

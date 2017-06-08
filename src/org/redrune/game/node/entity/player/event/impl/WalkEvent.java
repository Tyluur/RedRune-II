package org.redrune.game.node.entity.player.event.impl;

import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.Event;
import org.redrune.game.node.entity.player.event.EventPolicy.ActionPolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.AnimationPolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.InterfacePolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.WalkablePolicy;
import org.redrune.game.node.entity.player.event.context.WalkEventContext;
import org.redrune.game.node.entity.player.link.LockManager.LockType;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class WalkEvent extends Event<WalkEventContext> {
	
	@Override
	public boolean canStart(Player player) {
		return !player.getManager().getLocks().isLocked(LockType.MOVEMENT);
	}
	
	/**
	 * Constructs a new event
	 *
	 * @param context
	 * 		The context wrapper of the event
	 */
	public WalkEvent(WalkEventContext context) {
		super(context);
		setWalkablePolicy(WalkablePolicy.RESET);
		setActionPolicy(ActionPolicy.RESET);
		setAnimationPolicy(AnimationPolicy.RESET);
		setInterfacePolicy(InterfacePolicy.CLOSE);
	}
	
	@Override
	public void run(Player player) {
		int[] bufferX = getContext().getBufferX();
		int[] bufferY = getContext().getBufferY();
		int steps = getContext().getSteps();
		player.getMovement().reset(getContext().isRunning());
		int last = -1;
		for (int i = steps - 1; i >= 0; i--) {
			if (!player.getMovement().addWalkSteps(bufferX[i], bufferY[i], 25, true)) {
				break;
			}
			last = i;
		}
		
		if (last != -1) {
			Location tile = new Location(bufferX[last], bufferY[last], player.getLocation().getPlane());
			player.getTransmitter().sendMinimapFlag(tile.getLocalX(player.getLastLoadedLocation()), tile.getLocalY(player.getLastLoadedLocation()));
		} else {
			player.getTransmitter().sendMinimapFlagReset();
		}
	}
}
package org.redrune.game.node.entity.player.event.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.Event;
import org.redrune.game.node.entity.player.event.EventPolicy.ActionPolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.AnimationPolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.InterfacePolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.WalkablePolicy;
import org.redrune.game.node.entity.player.event.context.WalkEventContext;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.game.world.path.PathFactory;
import org.redrune.game.world.path.finder.DefaultPathFinder;

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
		player.getWalkingQueue().reset(getContext().isRunning());
		PathFactory.get().doPath(new DefaultPathFinder(), player, getContext().getX(), getContext().getY());
	}
}
package org.redrune.rs2.node.entity.player.event.impl;

import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.entity.player.event.Event;
import org.redrune.rs2.node.entity.player.event.EventPolicy.ActionPolicy;
import org.redrune.rs2.node.entity.player.event.EventPolicy.AnimationPolicy;
import org.redrune.rs2.node.entity.player.event.EventPolicy.InterfacePolicy;
import org.redrune.rs2.node.entity.player.event.context.WalkEventContext;
import org.redrune.rs2.world.map.path.PathFactory;
import org.redrune.rs2.world.map.path.finder.DefaultPathFinder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class WalkEvent extends Event<WalkEventContext> {
	
	/**
	 * Constructs a new event
	 *
	 * @param context
	 * 		The context wrapper of the event
	 */
	public WalkEvent(WalkEventContext context) {
		super(context);
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

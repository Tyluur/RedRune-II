package org.redrune.game.node.entity.player.event.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.Event;
import org.redrune.game.node.entity.player.event.EventPolicy.ActionPolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.AnimationPolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.InterfacePolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.WalkablePolicy;
import org.redrune.game.node.entity.player.event.context.NodeInteractionEventContext;
import org.redrune.game.world.path.NodeInteractionTask;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class NodeInteractionEvent extends Event<NodeInteractionEventContext> {
	
	/**
	 * Constructs a new event
	 *
	 * @param context
	 * 		The context wrapper of the event
	 */
	public NodeInteractionEvent(NodeInteractionEventContext context) {
		super(context);
		setWalkablePolicy(WalkablePolicy.RESET);
		setInterfacePolicy(InterfacePolicy.CLOSE);
		setAnimationPolicy(AnimationPolicy.RESET);
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player) {
		if (getContext().getNode().isNPC()) {
			player.turnTo(getContext().getNode().toNPC());
		}
		player.setInteractionTask(new NodeInteractionTask(getContext().getNode(), getContext().getTask()));
	}
}

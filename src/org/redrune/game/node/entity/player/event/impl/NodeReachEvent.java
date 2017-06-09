package org.redrune.game.node.entity.player.event.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.Event;
import org.redrune.game.node.entity.player.event.EventPolicy.ActionPolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.AnimationPolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.InterfacePolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.WalkablePolicy;
import org.redrune.game.node.entity.player.event.context.NodeReachEventContext;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.game.node.NodeInteractionTask;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class NodeReachEvent extends Event<NodeReachEventContext> {
	
	@Override
	public boolean canStart(Player player) {
		return !(getContext().getNode().isNPC() && player.getManager().getLocks().isLocked(LockType.NPC_INTERACTION) || getContext().getNode().isGameObject() && player.getManager().getLocks().isLocked(LockType.OBJECT_INTERACTION) || getContext().getNode().isItem() && player.getManager().getLocks().isLocked(LockType.ITEM_INTERACTION) || getContext().getNode().isPlayer() && player.getManager().getLocks().isLocked(LockType.PLAYER_INTERACTION));
	}
	
	/**
	 * Constructs a new event
	 *
	 * @param context
	 * 		The context wrapper of the event
	 */
	public NodeReachEvent(NodeReachEventContext context) {
		super(context);
		setWalkablePolicy(WalkablePolicy.RESET);
		setInterfacePolicy(InterfacePolicy.CLOSE);
		setAnimationPolicy(AnimationPolicy.RESET);
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player) {
		player.setInteractionTask(new NodeInteractionTask(getContext().getNode(), getContext().getTask(), getContext().getNode().isGameObject() || getContext().getNode().isNPC()));
		player.checkInteractionTask();
	}
}

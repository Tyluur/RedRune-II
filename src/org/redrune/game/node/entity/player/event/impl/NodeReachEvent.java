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
	
	/**
	 * Constructs a new event
	 */
	public NodeReachEvent() {
		setWalkablePolicy(WalkablePolicy.RESET);
		setInterfacePolicy(InterfacePolicy.CLOSE);
		setAnimationPolicy(AnimationPolicy.RESET);
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player, NodeReachEventContext context) {
		player.setInteractionTask(new NodeInteractionTask(context.getNode(), context.getTask(), context.getNode().isGameObject() || context.getNode().isNPC()));
		player.checkInteractionTask();
	}
	
	@Override
	public boolean canStart(Player player, NodeReachEventContext context) {
		return !(context.getNode().isNPC() && player.getManager().getLocks().isLocked(LockType.NPC_INTERACTION) || context.getNode().isGameObject() && player.getManager().getLocks().isLocked(LockType.OBJECT_INTERACTION) || context.getNode().isItem() && player.getManager().getLocks().isLocked(LockType.ITEM_INTERACTION) || context.getNode().isPlayer() && player.getManager().getLocks().isLocked(LockType.PLAYER_INTERACTION));
	}
}

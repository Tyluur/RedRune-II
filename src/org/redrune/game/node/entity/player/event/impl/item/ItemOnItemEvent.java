package org.redrune.game.node.entity.player.event.impl.item;

import org.redrune.game.content.skills.firemaking.Fire;
import org.redrune.game.content.skills.firemaking.FiremakingAction;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.Event;
import org.redrune.game.node.entity.player.event.EventPolicy.ActionPolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.AnimationPolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.InterfacePolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.WalkablePolicy;
import org.redrune.game.node.entity.player.event.context.item.ItemOnItemContext;
import org.redrune.game.node.item.Item;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
public class ItemOnItemEvent extends Event<ItemOnItemContext> {
	
	/**
	 * Constructs a new event
	 *
	 * @param context
	 * 		The context wrapper of the event
	 */
	public ItemOnItemEvent(ItemOnItemContext context) {
		super(context);
		setInterfacePolicy(InterfacePolicy.CLOSE);
		setAnimationPolicy(AnimationPolicy.RESET);
		setWalkablePolicy(WalkablePolicy.RESET);
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player) {
		int usedSlot = getContext().getUsedSlot();
		int withSlot = getContext().getWithSlot();
		
		Item usedItem = player.getInventory().getItems().get(usedSlot);
		Item withItem =  player.getInventory().getItems().get(withSlot);
		if (usedItem == null || withItem == null) {
			return;
		}
		
		Fire fire = FiremakingAction.getFire(player, usedItem, withItem);
		if (fire != null) {
			player.getManager().getActions().startAction(new FiremakingAction(fire));
		} else {
			player.getTransmitter().sendMessage("Nothing interesting happens.");
		}
	}
}

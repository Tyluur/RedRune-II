package org.redrune.rs2.node.entity.player.event.impl.item;

import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.entity.player.event.Event;
import org.redrune.rs2.node.entity.player.event.EventPolicy.ActionPolicy;
import org.redrune.rs2.node.entity.player.event.EventPolicy.AnimationPolicy;
import org.redrune.rs2.node.entity.player.event.EventPolicy.InterfacePolicy;
import org.redrune.rs2.node.entity.player.event.EventPolicy.WalkablePolicy;
import org.redrune.rs2.node.entity.player.event.context.item.ItemRemovalContext;
import org.redrune.rs2.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.rs2.node.item.Item;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/28/2017
 */
public class ItemRemovalEvent extends Event<ItemRemovalContext> {
	
	/**
	 * Constructs a new event
	 *
	 * @param context
	 * 		The context wrapper of the event
	 */
	public ItemRemovalEvent(ItemRemovalContext context) {
		super(context);
		setWalkablePolicy(WalkablePolicy.RESET);
		setInterfacePolicy(InterfacePolicy.CLOSE);
		setAnimationPolicy(AnimationPolicy.RESET);
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player) {
		int slotId = getContext().getSlotId();
		if (slotId >= 15) {
			return;
		}
		Item item = player.getEquipment().getItem(slotId);
		if (item == null || !player.getInventory().addItem(item.getId(), item.getAmount())) {
			return;
		}
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().sendContainer();
		player.getUpdateMasks().register(new AppearanceUpdate(player));
		if (slotId == 3) {
			// TODO: player.getCombatDefinitions().desecreaseSpecialAttack(0);
		}
	}
}

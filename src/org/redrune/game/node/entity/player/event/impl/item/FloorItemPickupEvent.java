package org.redrune.game.node.entity.player.event.impl.item;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.Event;
import org.redrune.game.node.entity.player.event.context.item.FloorItemPickupContext;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.game.node.item.FloorItem;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class FloorItemPickupEvent extends Event<FloorItemPickupContext> {
	
	@Override
	public boolean canStart(Player player) {
		return !player.getManager().getLocks().isLocked(LockType.ITEM_INTERACTION);
	}
	
	/**
	 * Constructs a new event
	 *
	 * @param context
	 * 		The context wrapper of the event
	 */
	public FloorItemPickupEvent(FloorItemPickupContext context) {
		super(context);
	}
	
	@Override
	public void run(Player player) {
		FloorItem floorItem = getContext().getFloorItem();
		Optional<FloorItem> optional = player.getRegion().getFloorItem(floorItem.getId(), floorItem.getLocation().getX(), floorItem.getLocation().getY(), floorItem.getLocation()
		.getPlane());
		if (!optional.isPresent()) {
			player.getTransmitter().sendMessage("Oops! You're too late!");
			return;
		}
		FloorItem found = optional.get();
		if (!player.getInventory().getItems().hasSpaceFor(found)) {
			player.getTransmitter().sendMessage("You don't have enough inventory space for that item.");
			return;
		}
		found.setRenderable(false);
		found.getRegion().removeFloorItem(found);
		player.getInventory().addItem(found.getId(), found.getAmount());
	}
}

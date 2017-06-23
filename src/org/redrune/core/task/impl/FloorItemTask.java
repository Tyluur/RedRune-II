package org.redrune.core.task.impl;

import org.redrune.core.task.ScheduledTask;
import org.redrune.game.node.item.FloorItem;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class FloorItemTask extends ScheduledTask {
	
	/**
	 * The floor item
	 */
	private final FloorItem item;
	
	public FloorItemTask(FloorItem item) {
		super(1, false);
		this.item = item;
	}
	
	@Override
	public Runnable getTask() {
		return () -> {
			item.addTicksPassed();
			// the time hasn't passed yet, hold on.
			if (!item.ticksElapsed()) {
				return;
			}
			// the item is a public item and its next phase is to delete
			if (item.isDefaultPublic()) {
				item.setRenderable(false);
				item.getRegion().removeFloorItem(item);
				stop();
			} else {
				// the item had an owner and its been alive for its destination ticks
				// the next phase is to remove after public for 3 minutes
				if (item.isOwnerVisibleOnly()) {
					// owner visible flag
					item.setOwnerVisibleOnly(false);
					
					// 300 ticks = 3 minutes
					item.setTicksPassed(0);
					item.setTargetTicks(300);
					
					// everyone else should see the item now
					item.getRegion().sendFloorItemToAll(item, true);
				} else {
					// the item's phase for being public to the owner only has already lapsed... removal now
					item.setRenderable(false);
					item.getRegion().removeFloorItem(item);
					stop();
				}
				// TODO fix: ground items showing up for other players when removed
				// http://i.imgur.com/3YCLp7M.png
			}
		};
	}
}
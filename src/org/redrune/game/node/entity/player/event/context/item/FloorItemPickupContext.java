package org.redrune.game.node.entity.player.event.context.item;

import lombok.Getter;
import org.redrune.game.node.entity.player.event.EventContext;
import org.redrune.game.node.item.FloorItem;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class FloorItemPickupContext implements EventContext {
	
	/**
	 * The item being picked up
	 */
	@Getter
	private final FloorItem floorItem;
	
	public FloorItemPickupContext(FloorItem floorItem) {
		this.floorItem = floorItem;
	}
}

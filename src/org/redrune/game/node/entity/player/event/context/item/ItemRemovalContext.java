package org.redrune.game.node.entity.player.event.context.item;

import lombok.Getter;
import org.redrune.game.node.entity.player.event.EventContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/28/2017
 */
public final class ItemRemovalContext implements EventContext {
	
	/**
	 * The slot id of the item to remove
	 */
	@Getter
	private final int slotId;
	
	public ItemRemovalContext(int slotId) {
		this.slotId = slotId;
	}
}

package org.redrune.game.content.entity.actor.player.event.item;

import org.redrune.game.content.entity.actor.player.event.Event;
import org.redrune.game.content.entity.item.InventoryOptionsHandler;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.utility.functions.Misc;

import static org.redrune.utility.constants.PacketConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-06
 */
public class ItemInteractionEvent extends Event {
	
	/**
	 * The item that was clicked
	 */
	private final Item item;
	
	/**
	 * The slot of the item that was clicked
	 */
	private final int slotId;
	
	/**
	 * The packet that was used when clicking the item
	 */
	private final int packetId;
	
	public ItemInteractionEvent(Item item, int slotId, int packetId) {
		this.item = item;
		this.slotId = slotId;
		this.packetId = packetId;
	}
	
	@Override
	public void run(Player player) {
		long time = Misc.currentTimeMillis();
		if (player.getLocks().isInteractionLocked() || player.getEmotesManager().getNextEmoteEnd() >= time) {
			return;
		}
		int itemId = item.getId();
		if (packetId == ACTION_BUTTON1_PACKET) {
			InventoryOptionsHandler.handleItemOption1(player, slotId, itemId, item);
		} else if (packetId == ACTION_BUTTON2_PACKET) {
			InventoryOptionsHandler.handleItemOption2(player, slotId, itemId, item);
		} else if (packetId == ACTION_BUTTON3_PACKET) {
			InventoryOptionsHandler.handleItemOption3(player, slotId, itemId, item);
		} else if (packetId == ACTION_BUTTON4_PACKET) {
			InventoryOptionsHandler.handleItemOption4(player, slotId, itemId, item);
		} else if (packetId == ACTION_BUTTON5_PACKET) {
			InventoryOptionsHandler.handleItemOption5(player, slotId, itemId, item);
		} else if (packetId == ACTION_BUTTON6_PACKET) {
			InventoryOptionsHandler.handleItemOption6(player, slotId, itemId, item);
		} else if (packetId == ACTION_BUTTON7_PACKET) {
			InventoryOptionsHandler.handleItemOption7(player, slotId, itemId, item);
		} else if (packetId == ACTION_BUTTON8_PACKET) {
			InventoryOptionsHandler.handleItemOption8(player, slotId, itemId, item);
		}
	}
	
	@Override
	public EventPolicy[] policies() {
		return arguments(EventPolicy.CLOSE_INTERFACE);
	}
}

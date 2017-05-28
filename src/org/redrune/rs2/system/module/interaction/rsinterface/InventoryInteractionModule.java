package org.redrune.rs2.system.module.interaction.rsinterface;

import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.entity.player.event.context.item.ItemEventContext;
import org.redrune.rs2.node.entity.player.event.impl.item.ItemEvent;
import org.redrune.rs2.node.item.Item;
import org.redrune.rs2.system.module.type.InterfaceInteractionModule;
import org.redrune.utility.Misc;
import org.redrune.utility.rs.InteractionOption;
import org.redrune.utility.rs.constant.InterfaceConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class InventoryInteractionModule implements InterfaceInteractionModule {
	
	/**
	 * The packet id for the first click on the item
	 */
	private static final int FIRST_PACKET_ID = 85;
	
	/**
	 * The packet id for equipping items (second click)
	 */
	private static final int EQUIP_PACKET_ID = 7;
	
	/**
	 * The packet id for operating items/third click
	 */
	private static final int OPERATE_PACKET_ID = 66;
	
	/**
	 * The packet id for the fourth item click
	 */
	private static final int FOURTH_PACKET_ID = 84;
	
	/**
	 * The packet id for the drop option
	 */
	private static final int DROP_PACKET_ID = 40;
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return Misc.arguments(InterfaceConstants.INVENTORY_INTERFACE_ID);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		//		System.out.println("player = [" + player + "], interfaceId = [" + interfaceId + "], componentId = [" + componentId + "], itemId = [" + itemId + "], slotId = [" + slotId + "], packetId = [" + packetId + "]");
		Item item = player.getInventory().getItems().get(slotId);
		if (item == null) {
			return true;
		}
		switch (packetId) {
			case FIRST_PACKET_ID:
				player.getEventManager().addEvent(new ItemEvent(new ItemEventContext(item, slotId, InteractionOption.FIRST_OPTION)));
				break;
			case EQUIP_PACKET_ID:
				player.getEventManager().addEvent(new ItemEvent(new ItemEventContext(item, slotId, InteractionOption.SECOND_OPTION)));
				break;
			case OPERATE_PACKET_ID:
				player.getEventManager().addEvent(new ItemEvent(new ItemEventContext(item, slotId, InteractionOption.THIRD_OPTION)));
				break;
			case FOURTH_PACKET_ID:
				player.getEventManager().addEvent(new ItemEvent(new ItemEventContext(item, slotId, InteractionOption.FOURTH_OPTION)));
				break;
			case DROP_PACKET_ID:
				player.getEventManager().addEvent(new ItemEvent(new ItemEventContext(item, slotId, InteractionOption.DROP)));
				break;
		}
		return true;
	}
}

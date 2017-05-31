package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.context.item.ItemEventContext;
import org.redrune.game.node.entity.player.event.impl.item.ItemEvent;
import org.redrune.game.node.item.Item;
import org.redrune.network.NetworkConstants;
import org.redrune.utility.Misc;
import org.redrune.utility.rs.InteractionOption;
import org.redrune.utility.rs.constant.InterfaceConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class InventoryInteractionModule implements InterfaceInteractionModule {
	
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
			case NetworkConstants.FIRST_PACKET_ID:
				player.getManager().getEvents().addEvent(new ItemEvent(new ItemEventContext(item, slotId, InteractionOption.FIRST_OPTION)));
				break;
			case NetworkConstants.EQUIP_PACKET_ID:
				player.getManager().getEvents().addEvent(new ItemEvent(new ItemEventContext(item, slotId, InteractionOption.SECOND_OPTION)));
				break;
			case NetworkConstants.OPERATE_PACKET_ID:
				player.getManager().getEvents().addEvent(new ItemEvent(new ItemEventContext(item, slotId, InteractionOption.THIRD_OPTION)));
				break;
			case NetworkConstants.FOURTH_PACKET_ID:
				player.getManager().getEvents().addEvent(new ItemEvent(new ItemEventContext(item, slotId, InteractionOption.FOURTH_OPTION)));
				break;
			case NetworkConstants.DROP_PACKET_ID:
				player.getManager().getEvents().addEvent(new ItemEvent(new ItemEventContext(item, slotId, InteractionOption.DROP)));
				break;
			case NetworkConstants.EXAMINE_PACKET_ID:
				player.getManager().getEvents().addEvent(new ItemEvent(new ItemEventContext(item, slotId, InteractionOption.EXAMINE)));
				break;
		}
		return true;
	}
}

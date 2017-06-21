package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.context.item.ItemEventContext;
import org.redrune.game.node.entity.player.event.impl.item.ItemEvent;
import org.redrune.game.node.entity.player.link.EventManager;
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
		InteractionOption option = getOption(packetId);
		if (option == null) {
			System.out.println("Unable to find interaction option for packet: " + packetId);
			return true;
		}
		EventManager.executeEvent(player, ItemEvent.class, new ItemEventContext(item, slotId, option));
		return true;
	}
	
	/**
	 * Gets the {@code InteractionOption} {code Object} by the packet id
	 *
	 * @param packetId
	 * 		The id of the packet
	 */
	private InteractionOption getOption(int packetId) {
		switch (packetId) {
			case NetworkConstants.FIRST_PACKET_ID:
				return InteractionOption.FIRST_OPTION;
			case NetworkConstants.EQUIP_PACKET_ID:
				return InteractionOption.SECOND_OPTION;
			case NetworkConstants.OPERATE_PACKET_ID:
				return InteractionOption.THIRD_OPTION;
			case NetworkConstants.FOURTH_PACKET_ID:
				return InteractionOption.FOURTH_OPTION;
			case NetworkConstants.DROP_PACKET_ID:
				return InteractionOption.DROP;
			case NetworkConstants.EXAMINE_PACKET_ID:
				return InteractionOption.EXAMINE;
			default:
				return null;
		}
	}
}

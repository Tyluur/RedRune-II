package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.NetworkConstants;
import org.redrune.utility.rs.input.InputResponse;
import org.redrune.utility.rs.input.InputType;

import static org.redrune.network.NetworkConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class BankInterfaceInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(762, 763);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == 762) { // bank
			if (componentId == 93) {
				switch (packetId) {
					case FIRST_PACKET_ID:
						player.getBank().withdrawItem(slotId, 1);
						return true;
					case SECOND_PACKET_ID:
						player.getBank().withdrawItem(slotId, 5);
						return true;
					case THIRD_PACKET_ID:
						player.getBank().withdrawItem(slotId, 10);
						return true;
					case LAST_PACKET_ID:
						player.getBank().withdrawItem(slotId, player.getBank().getDetails().getLastX());
						return true;
					case FIFTH_PACKET_ID:
						player.getTransmitter().requestInput(input -> {
							player.getBank().withdrawItem(slotId, InputResponse.getInput(input));
							player.getBank().getDetails().setLastX(InputResponse.getInput(input));
							player.getBank().refreshLastX();
						}, InputType.INTEGER, "Enter amount:");
						return true;
					case SIXTH_PACKET_ID:
						player.getBank().withdrawItem(slotId, Integer.MAX_VALUE);
						return true;
					case FOURTH_PACKET_ID:
						player.getBank().withdrawItemButOne(slotId);
						return true;
					case EXAMINE_PACKET_ID:
						// todo item examines
						return true;
				}
			} else if (componentId == 15) {
				player.getBank().switchInsertItems();
				return true;
			} else if (componentId == 19) {
				player.getBank().switchWithdrawNotes();
				return true;
			} else if (componentId == 33) {
				player.getBank().depositAllInventory();
				return true;
			} else if (componentId == 35) {
				player.getBank().depositAllEquipment();
				return true;
			} else if (componentId == 37) {
				//player.getBank().depositAllBob(true);
				return true;
			} else if (componentId == 44) { // '?'
				
				return true;
			} else if (componentId >= 46 && componentId <= 62) {
				int tabId = 9 - ((componentId - 44) / 2);
				if (packetId == NetworkConstants.FIRST_PACKET_ID) {
					player.getBank().setCurrentTab(tabId);
				} else if (packetId == NetworkConstants.SECOND_PACKET_ID) {
					player.getBank().collapse(tabId);
				}
				return true;
			}
		} else if (interfaceId == 763) { // bank inventory
			switch (packetId) {
				case FIRST_PACKET_ID:
					player.getBank().depositItem(slotId, 1, true);
					return true;
				case SECOND_PACKET_ID:
					player.getBank().depositItem(slotId, 5, true);
					return true;
				case THIRD_PACKET_ID:
					player.getBank().depositItem(slotId, 10, true);
					return true;
				case LAST_PACKET_ID:
					player.getBank().depositItem(slotId, player.getBank().getDetails().getLastX(), true);
					return true;
				case FIFTH_PACKET_ID:
					player.getTransmitter().requestInput(input -> {
						player.getBank().depositItem(slotId, InputResponse.getInput(input), true);
						player.getBank().getDetails().setLastX(InputResponse.getInput(input));
						player.getBank().refreshLastX();
					}, InputType.INTEGER, "Enter amount:");
					return true;
				case SIXTH_PACKET_ID:
					player.getBank().depositItem(slotId, Integer.MAX_VALUE, true);
					return true;
				case EXAMINE_PACKET_ID:
					// TODO item examines
					return true;
			}
		}
		System.out.println("interfaceId = [" + interfaceId + "], componentId = [" + componentId + "], itemId = [" + itemId + "], slotId = [" + slotId + "], packetId = [" + packetId + "]");
		return true;
	}
}

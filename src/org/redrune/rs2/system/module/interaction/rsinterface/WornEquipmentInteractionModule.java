package org.redrune.rs2.system.module.interaction.rsinterface;

import lombok.Getter;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.entity.player.event.context.item.ItemRemovalContext;
import org.redrune.rs2.node.entity.player.event.impl.item.ItemRemovalEvent;
import org.redrune.rs2.system.module.type.InterfaceInteractionModule;
import org.redrune.utility.Misc;
import org.redrune.utility.rs.constant.EquipConstants;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/28/2017
 */
public class WornEquipmentInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return Misc.arguments(387);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 39) { // stats
		
		} else if (componentId == 42) { // prices
		
		} else if (componentId == 45) { // ikod
		
		} else {
			// only thing left is removal of items
			Optional<SlotAction> optional = SlotAction.getSlotAction(componentId);
			if (!optional.isPresent()) {
				return true;
			}
			SlotAction action = optional.get();
			player.getEventManager().addEvent(new ItemRemovalEvent(new ItemRemovalContext(action.getEquipmentSlot())));
		}
		return true;
	}
	
	private enum SlotAction {
		
		HAT(8, EquipConstants.SLOT_HAT),
		CAPE(11, EquipConstants.SLOT_CAPE),
		AMULET(14, EquipConstants.SLOT_AMULET),
		ARROWS(38, EquipConstants.SLOT_ARROWS),
		WEAPON(17, EquipConstants.SLOT_WEAPON),
		CHEST(20, EquipConstants.SLOT_CHEST),
		SHIELD(23, EquipConstants.SLOT_SHIELD),
		LEGS(26, EquipConstants.SLOT_LEGS),
		HANDS(29, EquipConstants.SLOT_HANDS),
		FEET(32, EquipConstants.SLOT_FEET),
		RING(35, EquipConstants.SLOT_RING),
		AURA(50, EquipConstants.SLOT_AURA);
		
		@Getter
		private final int buttonId;
		
		@Getter
		private final int equipmentSlot;
		
		SlotAction(int buttonId, int equipmentSlot) {
			this.buttonId = buttonId;
			this.equipmentSlot = equipmentSlot;
		}
		
		/**
		 * Gets a slot action for the button clicked
		 *
		 * @param buttonId
		 * 		The button
		 */
		public static Optional<SlotAction> getSlotAction(int buttonId) {
			return Arrays.stream(SlotAction.values()).filter(p -> p.buttonId == buttonId).findFirst();
		}
		
		/**
		 * Handles other packet options
		 *
		 * @param player
		 * 		The player
		 * @param itemId
		 * 		The id of the item clicked
		 * @param packetId
		 * 		The id of the packet
		 */
		public boolean handleOtherOption(Player player, int itemId, int packetId) {
			return false;
		}
	}
}

package org.redrune.rs2.node.entity.player.components;

import lombok.Getter;
import lombok.Setter;
import org.redrune.network.rs666.packet.structure.out.ContainerPacketBuilder;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.item.Item;
import org.redrune.rs2.node.item.ItemsContainer;
import org.redrune.utility.rs.EquipConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class PlayerEquipment implements EquipConstants {
	
	/**
	 * The container of items
	 */
	@Getter
	private final ItemsContainer<Item> items = new ItemsContainer<>(15, false);
	
	/**
	 * The player
	 */
	@Setter
	private transient Player player;
	
	public PlayerEquipment() {
		items.set(SLOT_HAT, new Item(10828));
		items.set(SLOT_CHEST, new Item(10551));
		items.set(SLOT_LEGS, new Item(11726));
		items.set(SLOT_FEET, new Item(11732));
	}
	
	/**
	 * Sends the full container of items
	 */
	public void sendFullContainer() {
		player.getTransmitter().send(new ContainerPacketBuilder(94, items.toArray(), false).build(player));
	}
}

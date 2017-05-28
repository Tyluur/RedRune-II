package org.redrune.rs2.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.network.rs666.packet.structure.out.ContainerPacketBuilder;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.item.Item;
import org.redrune.rs2.node.item.ItemsContainer;
import org.redrune.utility.rs.constant.EquipConstants;

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
	
	}
	
	/**
	 * Sends the full container of items
	 */
	public void sendContainer() {
		player.getTransmitter().send(new ContainerPacketBuilder(94, items.toArray(), false).build(player));
	}
	
	/**
	 * Gets an item in the slot
	 *
	 * @param slot
	 * 		The slot
	 */
	public Item getItem(int slot) {
		return items.get(slot);
	}
	
	/**
	 * Checks that the player is equipping a shield
	 */
	public boolean hasShield() {
		return items.get(5) != null;
	}
	
}

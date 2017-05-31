package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.Cache;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.item.ItemsContainer;
import org.redrune.network.rs666.packet.structure.out.ContainerPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class PlayerInventory {
	
	/**
	 * The items in the container
	 */
	@Getter
	private ItemsContainer<Item> items;
	
	/**
	 * The player
	 */
	@Setter
	private transient Player player;
	
	public PlayerInventory() {
		items = new ItemsContainer<>(28, false);
	}
	
	/**
	 * Adds an item to the container
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param amount
	 * 		The amount of the item
	 */
	public boolean addItem(int itemId, int amount) {
		if (itemId < 0 || amount < 0 || itemId > Cache.getAmountOfItems()) {
			return false;
		}
		if (!items.add(new Item(itemId, amount))) {
			items.add(new Item(itemId, items.getFreeSlots()));
			player.getTransmitter().sendMessage("Not enough space in your inventory.", true);
			sendContainer();
			return false;
		}
		sendContainer();
		return true;
	}
	
	/**
	 * Sends the container items
	 */
	public void sendContainer() {
		player.getTransmitter().send(new ContainerPacketBuilder(93, items.toArray(), false).build(player));
	}
	
	/**
	 * Deletes an item from the container
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param amount
	 * 		The amount of the item to delete
	 */
	public boolean deleteItem(int itemId, int amount) {
		if (itemId < 0 || amount < 0 || itemId > Cache.getAmountOfItems()) {
			return false;
		}
		items.remove(new Item(itemId, amount));
		sendContainer();
		return true;
	}
	
	/**
	 * Deletes the item from the slot
	 *
	 * @param slotId
	 * 		The slot
	 */
	public void deleteSlotItem(int slotId) {
		items.set(slotId, null);
		sendContainer();
	}
	
	/**
	 * Switches items in slots
	 *
	 * @param fromSlot
	 * 		The slot the item is comign from
	 * @param toSlot
	 * 		The slot the item is going to
	 */
	public void switchItem(int fromSlot, int toSlot) {
		Item fromItem = items.get(fromSlot);
		Item toItem = items.get(toSlot);
		items.set(fromSlot, toItem);
		items.set(toSlot, fromItem);
		sendContainer();
	}
	
	/**
	 * Checks to make sure there are empty slots
	 */
	public boolean hasFreeSlots() {
		return items.getFreeSlot() != -1;
	}
}

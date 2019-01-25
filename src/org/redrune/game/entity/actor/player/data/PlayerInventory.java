package org.redrune.game.entity.actor.player.data;

import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.item.ItemsContainer;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository;

import java.io.Serializable;

public final class PlayerInventory implements Serializable {
	
	public static final int INVENTORY_INTERFACE = 679;
	
	private static final long serialVersionUID = 8842800123753277093L;
	
	private ItemsContainer<Item> items;
	
	private transient Player player;
	
	public PlayerInventory() {
		items = new ItemsContainer<Item>(28, false);
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void unlockInventoryOptions() {
		player.getPackets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 0, 27, 4554126);
		player.getPackets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 28, 55, 2097152);
	}
	
	public void reset() {
		items.reset();
		init(); // as all slots reseted better just send all again
	}
	
	public void init() {
		player.getPackets().sendItems(93, items);
	}
	
	@SuppressWarnings("rawtypes")
	public void addAll(ItemsContainer items) {
		if (items != null) {
			for (int i = 0; i < items.getSize(); i++) {
				if (items.get(i) != null) {
					this.items.add(items.get(i));
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void refresh(ItemsContainer items) {
		if (items != null && player != null) {
			player.getPackets().sendItems(93, items);
		}
	}
	
	public boolean addItem(int itemId, int amount) {
		if (itemId < 0 || amount < 0 || itemId >= Misc.getItemDefinitionsSize() || !player.getControllerManager().canAddInventoryItem(itemId, amount)) {
			return false;
		}
		Item[] itemsBefore = items.getItemsCopy();
		if (!items.add(new Item(itemId, amount))) {
			items.add(new Item(itemId, items.getFreeSlots()));
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			refreshItems(itemsBefore);
			return false;
		}
		refreshItems(itemsBefore);
		return true;
	}
	
	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index]) {
				changedSlots[count++] = index;
			}
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}
	
	public void refresh(int... slots) {
		player.getPackets().sendUpdateItems(93, items, slots);
	}
	
	public boolean addItem(Item item) {
		if (item.getId() < 0 || item.getAmount() < 0 || item.getId() >= Misc.getItemDefinitionsSize() || !player.getControllerManager().canAddInventoryItem(item.getId(), item.getAmount())) {
			return false;
		}
		Item[] itemsBefore = items.getItemsCopy();
		if (!items.add(item)) {
			items.add(new Item(item.getId(), items.getFreeSlots()));
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			refreshItems(itemsBefore);
			return false;
		}
		refreshItems(itemsBefore);
		return true;
	}
	
	public boolean removeItems(Item... list) {
		if (list == null || list.length == 0) {
			return false;
		}
		for (Item item : list) {
			if (item != null) {
				deleteItem(items.getThisItemSlot(item), item);
			}
		}
		refresh();
		return true;
	}
	
	public void deleteItem(int slot, Item item) {
		if (!player.getControllerManager().canDeleteInventoryItem(item.getId(), item.getAmount())) {
			return;
		}
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(slot, item);
		refreshItems(itemsBefore);
	}
	
	public void deleteItem(int itemId, int amount) {
		if (!player.getControllerManager().canDeleteInventoryItem(itemId, amount)) {
			return;
		}
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
	}
	
	public void deleteItem(Item item) {
		if (!player.getControllerManager().canDeleteInventoryItem(item.getId(), item.getAmount())) {
			return;
		}
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(item);
		refreshItems(itemsBefore);
	}
	
	/*
	 * No refresh needed its client to who does it :p
	 */
	public void switchItem(int fromSlot, int toSlot) {
		Item[] itemsBefore = items.getItemsCopy();
		Item fromItem = items.get(fromSlot);
		Item toItem = items.get(toSlot);
		items.set(fromSlot, toItem);
		items.set(toSlot, fromItem);
		refreshItems(itemsBefore);
	}
	
	public ItemsContainer<Item> getItems() {
		return items;
	}
	
	public boolean hasFreeSlots() {
		return items.getFreeSlot() != -1;
	}
	
	public int getFreeSlots() {
		return items.getFreeSlots();
	}
	
	public int getNumerOf(int itemId) {
		return items.getNumberOf(itemId);
	}
	
	public Item getItem(int slot) {
		return items.get(slot);
	}
	
	public boolean containsItems(Item[] item) {
		for (int i = 0; i < item.length; i++) {
			if (!items.contains(item[i])) {
				return false;
			}
		}
		return true;
	}
	
	public boolean containsItems(int[] itemIds, int[] ammounts) {
		int size = itemIds.length > ammounts.length ? ammounts.length : itemIds.length;
		for (int i = 0; i < size; i++) {
			if (!items.contains(new Item(itemIds[i], ammounts[i]))) {
				return false;
			}
		}
		return true;
	}
	
	public boolean containsItem(int itemId, int ammount) {
		return items.contains(new Item(itemId, ammount));
	}
	
	public boolean containsOneItem(int... itemIds) {
		for (int itemId : itemIds) {
			if (items.containsOne(new Item(itemId, 1))) {
				return true;
			}
		}
		return false;
	}
	
	public int numberOf(int id) {
		return items.getNumberOf(new Item(id, 1));
	}
	
	public void sendExamine(int slotId) {
		if (slotId >= getItemsContainerSize()) {
			return;
		}
		Item item = items.get(slotId);
		if (item == null) {
			return;
		}
		player.getPackets().sendGameMessage(ItemCharacteristicRepository.getExamine(item.getId()));
	}
	
	public int getItemsContainerSize() {
		return items.getSize();
	}
	
	
	public boolean addItemDrop(Item item) {
		if (item.getId() < 0 || item.getAmount() < 0 || !Misc.itemExists(item.getId()) || !player.getControllerManager().canAddInventoryItem(item.getId(), item.getAmount())) {
			return false;
		}
		Item[] itemsBefore = items.getItemsCopy();
		WorldTile tile = player;
		if (!items.add(item)) {
			if (item.getDefinitions().isStackable()) {
				RegionManager.addGroundItem(item, tile, player, true, 180, 3, 150);
			} else {
				for (int i = 0; i < item.getAmount(); i++) {
					RegionManager.addGroundItem(new Item(item.getId(), 1), tile, player, true, 180, 3, 150);
				}
			}
			String name = item.getName();
			String formattedName = name + (item.getAmount() > 1 ? (name.endsWith("s") ? "" : "s") : "");
			player.getPackets().sendGameMessage(item.getAmount() + " " + formattedName + " have been dropped to your feet because your inventory was full.");
		} else {
			refreshItems(itemsBefore);
		}
		return true;
	}
}
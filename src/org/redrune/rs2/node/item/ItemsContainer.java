package org.redrune.rs2.node.item;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Container class.
 *
 * @author Graham / edited by Dragonkk(Alex)
 */
public final class ItemsContainer<T extends Item> implements Serializable {
	
	private static final long serialVersionUID = 1099313426737026107L;
	
	private Item[] data;
	
	private boolean alwaysStackable = false;
	
	public ItemsContainer(int size, boolean alwaysStackable) {
		data = new Item[size];
		this.alwaysStackable = alwaysStackable;
	}
	
	@Override
	public String toString() {
		String items = "";
		for (int i = 0; i < data.length; i++) {
			Item item = data[i];
			if (item == null) {
				continue;
			}
			items += item.toString();
		}
		return "ItemsContainer[" + items + "]";
	}
	
	public void shift() {
		Item[] oldData = data;
		data = new Item[oldData.length];
		int ptr = 0;
		for (int i = 0; i < data.length; i++) {
			if (oldData[i] != null) {
				data[ptr++] = oldData[i];
			}
		}
	}
	
	public boolean forceAdd(T item) {
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null) {
				data[i] = item;
				return true;
			}
		}
		return false;
	}
	
	public boolean canAdd(T item) {
		if (alwaysStackable || item.getDefinitions().isStackable() || item.getDefinitions().isNoted()) {
			for (Item aData : data) {
				if (aData != null) {
					if (aData.getId() == item.getId()) {
						return true;
					}
				}
			}
		} else {
			if (item.getAmount() > 1) {
				return freeSlots() >= item.getAmount();
			}
		}
		int index = freeSlot();
		return index != -1;
	}
	
	public int freeSlots() {
		int j = 0;
		for (Item aData : data) {
			if (aData == null) {
				j++;
			}
		}
		return j;
	}
	
	public int freeSlot() {
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public int remove(T item) {
		int removed = 0, toRemove = item.getAmount();
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				if (data[i].getId() == item.getId()) {
					int amt = data[i].getAmount();
					if (amt > toRemove) {
						removed += toRemove;
						amt -= toRemove;
						toRemove = 0;
						data[i] = new Item(data[i].getId(), amt);
						return removed;
					} else {
						removed += amt;
						toRemove -= amt;
						data[i] = null;
					}
				}
			}
		}
		return removed;
	}
	
	public void removeAll(T item) {
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				if (data[i].getId() == item.getId()) {
					data[i] = null;
				}
			}
		}
	}
	
	public boolean containsOne(T item) {
		for (Item aData : data) {
			if (aData != null) {
				if (aData.getId() == item.getId()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean contains(int itemId) {
		for (Item aData : data) {
			if (aData != null) {
				if (aData.getId() == itemId) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean contains(T item) {
		int amtOf = 0;
		for (Item aData : data) {
			if (aData != null) {
				if (aData.getId() == item.getId()) {
					amtOf += aData.getAmount();
				}
			}
		}
		return amtOf >= item.getAmount();
	}
	
	public void clear() {
		for (int i = 0; i < data.length; i++) {
			data[i] = null;
		}
	}
	
	public int getFreeSlots() {
		int s = 0;
		for (Item aData : data) {
			if (aData == null) {
				s++;
			}
		}
		return s;
	}
	
	public int getUsedSlots() {
		int s = 0;
		for (Item aData : data) {
			if (aData != null) {
				s++;
			}
		}
		return s;
	}
	
	public int getNumberOf(Item item) {
		int count = 0;
		for (Item aData : data) {
			if (aData != null) {
				if (aData.getId() == item.getId()) {
					count += aData.getAmount();
				}
			}
		}
		return count;
	}
	
	public int getNumberOf(int item) {
		int count = 0;
		for (Item aData : data) {
			if (aData != null) {
				if (aData.getId() == item) {
					count += aData.getAmount();
				}
			}
		}
		return count;
	}
	
	public Item[] getItems() {
		return data;
	}
	
	public Item[] getItemsCopy() {
		Item[] newData = new Item[data.length];
		System.arraycopy(data, 0, newData, 0, newData.length);
		return newData;
	}
	
	public ItemsContainer<Item> asItemContainer() {
		ItemsContainer<Item> c = new ItemsContainer<Item>(data.length, this.alwaysStackable);
		System.arraycopy(data, 0, c.data, 0, data.length);
		return c;
	}
	
	public int getThisItemSlot(T item) {
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				if (data[i].getId() == item.getId()) {
					return i;
				}
			}
		}
		return getFreeSlot();
	}
	
	public int getFreeSlot() {
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public Item lookup(int id) {
		for (Item aData : data) {
			if (aData == null) {
				continue;
			}
			if (aData.getId() == id) {
				return aData;
			}
		}
		return null;
	}
	
	public int lookupSlot(int id) {
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null) {
				continue;
			}
			if (data[i].getId() == id) {
				return i;
			}
		}
		return -1;
	}
	
	public int lookupSlot(Item item) {
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null) {
				continue;
			}
			if (data[i].equals(item)) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean replace(int slotId, int itemId, int itemAmount) {
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null) {
				continue;
			}
			if (i == slotId) {
				data[i] = new Item(itemId, itemAmount);
				return true;
			}
		}
		return false;
	}
	
	public void reset() {
		data = new Item[data.length];
	}
	
	public int remove(int preferredSlot, Item item) {
		int removed = 0, toRemove = item.getAmount();
		if (data[preferredSlot] != null) {
			if (data[preferredSlot].getId() == item.getId()) {
				int amt = data[preferredSlot].getAmount();
				if (amt > toRemove) {
					removed += toRemove;
					amt -= toRemove;
					toRemove = 0;
					// data[preferredSlot] = new
					// Item(data[preferredSlot].getDefinition().getApplicableIds(), amt);
					set2(preferredSlot, new Item(data[preferredSlot].getId(), amt));
					return removed;
				} else {
					removed += amt;
					toRemove -= amt;
					// data[preferredSlot] = null;
					set(preferredSlot, null);
				}
			}
		}
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				if (data[i].getId() == item.getId()) {
					int amt = data[i].getAmount();
					if (amt > toRemove) {
						removed += toRemove;
						amt -= toRemove;
						toRemove = 0;
						// data[i] = new Item(data[i].getDefinition().getApplicableIds(),
						// amt);
						set2(i, new Item(data[i].getId(), amt));
						return removed;
					} else {
						removed += amt;
						toRemove -= amt;
						// data[i] = null;
						set(i, null);
					}
				}
			}
		}
		return removed;
	}
	
	public void set2(int slot, Item item) {
		if (slot < 0 || slot >= data.length) {
			return;
		}
		data[slot] = item;
	}
	
	public void set(int slot, T item) {
		if (slot < 0 || slot >= data.length) {
			return;
		}
		data[slot] = item;
	}
	
	public void addAll(ItemsContainer<T> container) {
		for (int i = 0; i < container.getSize(); i++) {
			T item = container.get(i);
			if (item != null) {
				this.add(item);
			}
		}
	}
	
	public int getSize() {
		return data.length;
	}
	
	@SuppressWarnings("unchecked")
	public T get(int slot) {
		if (slot < 0 || slot >= data.length) {
			return null;
		}
		return (T) data[slot];
	}
	
	public boolean add(T item) {
		if (alwaysStackable || item.getDefinitions().isStackable() || item.getDefinitions().isNoted()) {
			for (int i = 0; i < data.length; i++) {
				if (data[i] != null) {
					if (data[i].getId() == item.getId()) {
						long newTotal = (long) (data[i].getAmount() + item.getAmount());
						if (newTotal < 0 || newTotal > Integer.MAX_VALUE) {
							return false;
						}
						data[i] = new Item(data[i].getId(), (int) newTotal);
						return true;
					}
				}
			}
		} else {
			if (item.getAmount() > 1) {
				if (freeSlots() >= item.getAmount()) {
					for (int i = 0; i < item.getAmount(); i++) {
						int index = freeSlot();
						data[index] = new Item(item.getId(), 1);
					}
					return true;
				} else {
					return false;
				}
			}
		}
		int index = freeSlot();
		if (index == -1) {
			return false;
		}
		data[index] = item;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void addAll(Item[] container) {
		for (int i = 0; i < container.length; i++) {
			Item item = container[i];
			if (item != null) {
				this.add((T) item);
			}
		}
	}
	
	public boolean hasSpaceFor(ItemsContainer<T> container) {
		for (int i = 0; i < container.getSize(); i++) {
			T item = container.get(i);
			if (item != null) {
				if (!this.hasSpaceForItem(item)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean hasSpaceForItem(T item) {
		if (alwaysStackable || item.getDefinitions().isStackable() || item.getDefinitions().isNoted()) {
			for (Item aData : data) {
				if (aData != null) {
					if (aData.getId() == item.getId()) {
						return true;
					}
				}
			}
		} else {
			if (item.getAmount() > 1) {
				return freeSlots() >= item.getAmount();
			}
		}
		int index = freeSlot();
		return index != -1;
	}
	
	public Item[] toArray() {
		return data;
	}
	
	/**
	 * Creates a stream of all the items in the players inventory that are not nulled
	 */
	public Stream<Item> stream() {
		return Arrays.stream(data).filter(Objects::nonNull);
	}
	
	@SuppressWarnings("unchecked")
	public boolean hasSpaceFor(Item item) {
		return hasSpaceForItem((T) item);
	}
	
	/**
	 * Calculates the size of the container
	 */
	public int size() {
		int size = 0;
		for (Item item : data) {
			if (item == null) {
				continue;
			}
			size++;
		}
		return size;
	}
}

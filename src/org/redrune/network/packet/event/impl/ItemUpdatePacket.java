package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;
import org.redrune.rs2.node.item.Item;
import org.redrune.rs2.node.item.ItemsContainer;

/**
 * ItemUpdatePacket.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class ItemUpdatePacket implements PacketEvent {

	private final int opcode;

	private final boolean key;

	private final Item[] items;

	private final int[] slots;

	public ItemUpdatePacket(int opcode, ItemsContainer<Item> items, int... slots) {
		this(opcode, items.getItems(), slots);
	}

	public ItemUpdatePacket(int opcode, Item[] items, int... slots) {
		this(opcode, opcode < 0, items, slots);
	}

	public ItemUpdatePacket(int opcode, boolean key, Item[] items, int... slots) {
		this.opcode = opcode;
		this.key = key;
		this.items = items;
		this.slots = slots;
	}

	public int getOpcode() {
		return opcode;
	}

	public boolean isKey() {
		return key;
	}

	public Item[] getItems() {
		return items;
	}

	public int[] getSlots() {
		return slots;
	}

}

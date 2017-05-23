package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;
import org.redrune.rs2.node.item.Item;
import org.redrune.rs2.node.item.ItemsContainer;

/**
 * ItemPacket.java
 * 
 * @author Chryonic May 22, 2017 | RedRune
 */
public class ItemPacket implements PacketContext {

	private final int opcode;

	private final boolean key;

	private final Item[] items;

	public ItemPacket(int opcode, ItemsContainer<Item> items) {
		this(opcode, items.getItems());
	}

	public ItemPacket(int opcode, Item[] items) {
		this(opcode, opcode < 0, items);
	}

	public ItemPacket(int opcode, boolean key, Item[] items) {
		this.opcode = opcode;
		this.key = key;
		this.items = items;
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

}

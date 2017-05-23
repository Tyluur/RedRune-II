package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class PlayerOptionPacket implements PacketContext {

	private final String option;

	private final int slot;

	private final boolean isTopOption;

	private final int cursor;

	public PlayerOptionPacket(String option, int slot) {
		this(option, slot, false, -1);
	}

	public PlayerOptionPacket(String option, int slot, boolean isTopOption, int cursor) {
		this.option = option;
		this.slot = slot;
		this.isTopOption = isTopOption;
		this.cursor = cursor;
	}

	public String getOption() {
		return option;
	}

	public int getSlot() {
		return slot;
	}

	public boolean isTopOption() {
		return isTopOption;
	}

	public int getCursor() {
		return cursor;
	}

}

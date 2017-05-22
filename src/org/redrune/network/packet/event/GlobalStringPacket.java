package org.redrune.network.packet.event;

public class GlobalStringPacket implements PacketEvent {

	private final int id;

	private final String text;

	public GlobalStringPacket(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

}

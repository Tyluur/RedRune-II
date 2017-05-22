package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

public class MessagePacket implements PacketEvent {

	private final String message;

	private final int channel;

	public MessagePacket(String message, int channel) {
		this.message = message;
		this.channel = channel;
	}

	public String getMessage() {
		return message;
	}

	public int getChannel() {
		return channel;
	}

}

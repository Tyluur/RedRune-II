package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class MessagePacket implements PacketContext {

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

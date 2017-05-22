package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

public class OpenWebsitePacket implements PacketEvent {

	private final String address;

	public OpenWebsitePacket(String address) {
		this.address = address;
	}

	public String getWebsiteAddress() {
		return address;
	}

}

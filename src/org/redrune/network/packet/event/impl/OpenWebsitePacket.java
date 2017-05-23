package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class OpenWebsitePacket implements PacketContext {

	private final String address;

	public OpenWebsitePacket(String address) {
		this.address = address;
	}

	public String getWebsiteAddress() {
		return address;
	}

}

package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

/**
 * KeepAlivePacket.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class KeepAlivePacket implements PacketEvent {

	private final int ping;

	public KeepAlivePacket(int ping) {
		this.ping = ping;
	}

	public int getPing() {
		return ping;
	}

}

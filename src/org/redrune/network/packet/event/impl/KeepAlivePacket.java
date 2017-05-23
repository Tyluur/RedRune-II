package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

/**
 * KeepAlivePacket.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class KeepAlivePacket implements PacketContext {

	private final int ping;

	public KeepAlivePacket(int ping) {
		this.ping = ping;
	}

	public int getPing() {
		return ping;
	}

}

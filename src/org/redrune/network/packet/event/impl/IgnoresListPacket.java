package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

import jdk.nashorn.internal.ir.annotations.Ignore;

public class IgnoresListPacket implements PacketContext {

	private final Ignore ignore;

	public IgnoresListPacket(Ignore ignore) {
		this.ignore = ignore;
	}

	public Ignore getIgnore() {
		return ignore;
	}

}

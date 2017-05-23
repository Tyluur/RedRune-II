package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class CS2ScriptPacket implements PacketContext {

	private final int scriptId;

	private final Object[] parameters;

	public CS2ScriptPacket(int scriptId, Object[] parameters) {
		this.scriptId = scriptId;
		this.parameters = parameters;
	}

	public int getScriptId() {
		return scriptId;
	}

	public Object[] getParameters() {
		return parameters;
	}

}

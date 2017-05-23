package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class MinimapFlagPacket implements PacketContext {

	private final int coordX;

	private final int coordY;

	public MinimapFlagPacket(int coordX, int coordY) {
		this.coordX = coordX;
		this.coordY = coordY;
	}

	public int getCoordX() {
		return coordX;
	}

	public int getCoordY() {
		return coordY;
	}

}

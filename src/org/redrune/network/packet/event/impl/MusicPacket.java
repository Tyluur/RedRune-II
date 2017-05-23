package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class MusicPacket implements PacketContext {

	private final int songId;

	private final int volume;

	private final int songDelay;

	public MusicPacket(int songId, int volume, int songDelay) {
		this.songId = songId;
		this.volume = volume;
		this.songDelay = songDelay;
	}

	public int getSongId() {
		return songId;
	}

	public int getVolume() {
		return volume;
	}

	public int getSongDelay() {
		return songDelay;
	}

}

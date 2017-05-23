package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class SoundPacket implements PacketContext {

	private final int soundId;

	private final int soundDelay;

	private final int effectId;

	public SoundPacket(int soundId, int soundDelay, int effectId) {
		this.soundId = soundId;
		this.soundDelay = soundDelay;
		this.effectId = effectId;
	}

	public int getSoundId() {
		return soundId;
	}

	public int getSoundDelay() {
		return soundDelay;
	}

	public int getEffectId() {
		return effectId;
	}

}

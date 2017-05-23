package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketContext;

public class AnimationOnChildPacket implements PacketContext {

	private final int interfaceId;

	private final int childId;

	private final int animationId;

	public AnimationOnChildPacket(int interfaceId, int childId, int animationId) {
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.animationId = animationId;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getChildId() {
		return childId;
	}

	public int getAnimationId() {
		return animationId;
	}

}

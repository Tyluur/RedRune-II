package org.redrune.network.protocol.game.msg;

import org.redrune.network.stream.IoWriteEvent;
import org.redrune.utility.backend.isaac.IsaacRandomPair;

/**
 * GameResponseEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class GameResponseEvent {

	private final IsaacRandomPair isaacPair;

	private final Class<?> clazz;

	private final IoWriteEvent event;

	public GameResponseEvent(IsaacRandomPair isaacPair, Class<?> clazz, IoWriteEvent event) {
		this.isaacPair = isaacPair;
		this.clazz = clazz;
		this.event = event;
	}

	public IsaacRandomPair getIsaacPair() {
		return isaacPair;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public IoWriteEvent getIOWriteEvent() {
		return event;
	}

}

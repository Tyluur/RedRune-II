package org.redrune.network.protocol.game.msg;

import org.redrune.network.stream.IoReadEvent;

/**
 * GameRequestEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class GameRequestEvent {

	private final IoReadEvent event;

	public GameRequestEvent(IoReadEvent event) {
		this.event = event;
	}

	public IoReadEvent getIOReadEvent() {
		return event;
	}

}

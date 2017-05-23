package org.redrune.network.protocol.handshake.msg;

import lombok.Getter;

/**
 * HSRequestEvent.java
 *
 * @author Chryonic May 22, 2017 | RedRune
 */
public class HSRequestEvent {
	
	@Getter
	private final int major;
	
	public HSRequestEvent(int major) {
		this.major = major;
	}
	
}

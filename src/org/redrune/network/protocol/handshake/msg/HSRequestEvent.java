package org.redrune.network.protocol.handshake.msg;

import lombok.Getter;

/**
 * HSRequestEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class HSRequestEvent {

	@Getter
	private final int major;

	@Getter
	private final int minor;

	@Getter
	private final String key;

	public HSRequestEvent(int major, int minor, String key) {
		this.major = major;
		this.minor = minor;
		this.key = key;
	}


}

package org.redrune.network.protocol.handshake.msg;

import org.redrune.network.protocol.ProtocolResponse;

/**
 * HSResponseEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class HSResponseEvent {

	private final ProtocolResponse response;

	public HSResponseEvent(ProtocolResponse response) {
		this.response = response;
	}

	public ProtocolResponse getResponse() {
		return response;
	}

}

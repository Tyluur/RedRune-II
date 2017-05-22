package org.redrune.network.session.impl;

import org.redrune.network.protocol.Protocol;
import org.redrune.network.protocol.ProtocolResponse;
import org.redrune.network.protocol.handshake.msg.HSRequestEvent;
import org.redrune.network.protocol.handshake.msg.HSResponseEvent;
import org.redrune.network.session.Session;

import io.netty.channel.Channel;

/**
 * HandshakeSession.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class HandshakeSession extends Session {

	public HandshakeSession(Channel channel) {
		super(channel);
	}

	@Override
	public void throttleRequest(Object context) {
		if (context instanceof HSRequestEvent) {
			HSRequestEvent request = (HSRequestEvent) context;
			if (request.getMajor() != Protocol.REVISION || request.getMinor() != Protocol.SUB_REVISION
					|| !request.getKey().equals(Protocol.PREFETCH_KEY)) {
				channel.writeAndFlush(new HSResponseEvent(ProtocolResponse.OUT_OF_DATE));
			} else {
				channel.writeAndFlush(new HSResponseEvent(ProtocolResponse.SUCCESSFUL_CONNECTION));
			}
		}
	}

	@Override
	public void disconnect() throws InterruptedException {
		// channel.disconnect().sync();
	}

}

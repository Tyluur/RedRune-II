package org.redrune.network.session.impl;

import io.netty.channel.Channel;
import org.redrune.network.NetworkConstants;
import org.redrune.network.protocol.ProtocolResponse;
import org.redrune.network.protocol.handshake.msg.HSRequestEvent;
import org.redrune.network.protocol.handshake.msg.HSResponseEvent;
import org.redrune.network.session.Session;

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
			System.out.println(request.getMajor());
			if (request.getMajor() != NetworkConstants.REVISION) {
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

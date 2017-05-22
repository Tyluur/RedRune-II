package org.redrune.network.session;

import io.netty.channel.Channel;

/**
 * Session.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public abstract class Session {

	protected final Channel channel;

	public Session(Channel channel) {
		this.channel = channel;
	}

	public abstract void throttleRequest(Object context);

	public abstract void disconnect() throws InterruptedException;

	public Channel getChannel() {
		return channel;
	}

}

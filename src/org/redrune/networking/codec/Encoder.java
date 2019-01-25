package org.redrune.networking.codec;

import org.redrune.networking.Session;

public abstract class Encoder {
	
	protected Session session;
	
	public Encoder(Session session) {
		this.session = session;
	}
	
}

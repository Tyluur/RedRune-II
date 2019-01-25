package org.redrune.networking.codec;

import org.redrune.networking.Session;
import org.redrune.networking.stream.InputStream;

public abstract class Decoder {
	
	public abstract void decode(InputStream stream);
	
	protected Session session;
	
	public Decoder(Session session) {
		this.session = session;
	}
	
}

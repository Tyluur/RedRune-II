package com.rs.networking.codec;

import com.rs.networking.Session;
import com.rs.networking.io.InputStream;

public abstract class Decoder {
	
	protected Session session;
	
	public Decoder(Session session) {
		this.session = session;
	}
	
	public abstract void decode(InputStream stream);
	
}

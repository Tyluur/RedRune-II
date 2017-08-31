package com.rs.networking.codec;

import com.rs.networking.Session;

public abstract class Encoder {

	protected Session session;

	public Encoder(Session session) {
		this.session = session;
	}

}

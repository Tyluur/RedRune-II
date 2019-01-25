package org.redrune.networking.codec.decode;

import org.redrune.utility.constants.NetworkConstants;
import org.redrune.networking.Session;
import org.redrune.networking.codec.Decoder;
import org.redrune.networking.stream.InputStream;

public final class ClientPacketsDecoder extends Decoder {
	
	public ClientPacketsDecoder(Session connection) {
		super(connection);
	}
	
	@Override
	public final void decode(InputStream stream) {
		session.setDecoder(-1);
		int packetId = stream.readUnsignedByte();
		switch (packetId) {
			case 14:
				decodeLogin(stream);
				break;
			case 15:
				decodeGrab(stream);
				break;
			default:
				session.getChannel().close();
				break;
		}
	}
	
	private void decodeLogin(InputStream stream) {
		if (stream.getRemaining() != 0) {
			session.getChannel().close();
			return;
		}
		session.setDecoder(2);
		session.setEncoder(1);
		session.getLoginPackets().sendStartUpPacket();
	}
	
	private void decodeGrab(InputStream stream) {
		if (stream.getRemaining() != 4) {
			session.getChannel().close();
			System.out.println("Bad remaining");
			return;
		}
		session.setEncoder(0);
		int protocol = stream.readInt();
		if (protocol != NetworkConstants.PROTOCOL_NUMBER) {
			session.setDecoder(-1);
			session.getGrabPackets().sendOutdatedClientPacket();
			System.out.println("bad protocol: " + protocol);
			return;
		}
		session.setDecoder(1);
		session.getGrabPackets().sendStartUpPacket();
	}
}

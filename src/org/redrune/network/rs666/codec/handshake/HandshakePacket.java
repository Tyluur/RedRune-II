package org.redrune.network.rs666.codec.handshake;

import lombok.Getter;
import org.redrune.network.rs666.packet.Packet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class HandshakePacket {
	
	/**
	 * The opcode of the handshake
	 */
	@Getter
	private final int opcode;
	
	/**
	 * The packet of the handshake
	 */
	@Getter
	private final Packet packet;
	
	HandshakePacket(int opcode, Packet buffer) {
		this.opcode = opcode;
		this.packet = buffer;
	}
	
}

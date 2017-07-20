package org.redrune.network.master.server.network.packet.out;

import org.redrune.network.master.network.packet.writeable.WriteablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class SuccessfulVerificationOut extends WriteablePacket {
	
	/**
	 * If the world was also created during the verification process. This should always be true, unless the world was
	 * already existent.
	 */
	private final boolean success;
	
	/**
	 * Constructs a new outgoing packet
	 */
	public SuccessfulVerificationOut(boolean success) {
		super(SUCCESSFUL_VERIFICATION_PACKET_ID);
		this.success = success;
	}
	
	@Override
	public WriteablePacket create() {
		writeByte((byte) (success ? 1 : 0));
		writeString(WELCOME_MESSAGE);
		return this;
	}
}

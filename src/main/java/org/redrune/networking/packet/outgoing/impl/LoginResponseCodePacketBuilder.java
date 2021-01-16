package org.redrune.networking.packet.outgoing.impl;

import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.PacketBuilder;
import org.redrune.networking.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.game.entity.actor.player.LoginReturnCode;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
public class LoginResponseCodePacketBuilder extends OutgoingPacketBuilder {
	
	/**
	 * The byte value of the response code
	 */
	private final int responseCode;
	
	/**
	 * Constructs a new login response packet with a {@code LoginReturnCode} {@code Object}
	 */
	public LoginResponseCodePacketBuilder(LoginReturnCode code) {
		this(code.getValue());
	}
	
	/**
	 * Constructs a new login response packet with the numerical value of the response code. See {@link LoginReturnCode}
	 * for the possible values
	 */
	public LoginResponseCodePacketBuilder(int responseCode) {
		super(new PacketBuilder());
		this.responseCode = responseCode;
	}
	
	@Override
	public Packet build() {
		bldr.writeByte(responseCode);
		return bldr.toPacket();
	}
}

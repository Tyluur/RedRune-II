package org.redrune.network.rs666.packet.structure.out;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.utility.backend.ReturnCode;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class LoginResponseCodeBuilder implements OutgoingPacketStructure {
	
	private final ReturnCode returnCode;
	
	public LoginResponseCodeBuilder(ReturnCode returnCode) {
		this.returnCode = returnCode;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder builder = new PacketBuilder();
		builder.writeByte(returnCode.getValue());
		return builder.toPacket();
	}
}

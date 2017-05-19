package org.redrune.network.rs666.packet.structure.out;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public class LoginCredentialsBuilder implements OutgoingPacketStructure {
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder();
		bldr.writeByte(13 + 1); //length
		bldr.writeByte((byte) player.getCredentials().getDominantRight().getClientRight());
		bldr.writeByte(0);
		bldr.writeByte(0);
		bldr.writeByte(0);
		bldr.writeByte(1);
		bldr.writeByte(0);
		bldr.writeShort(player.getIndex());
		bldr.writeByte(1);
		bldr.writeMedium(0);
		bldr.writeByte(1); // members
		bldr.writeRS2String("");
		return bldr.toPacket();
	}
}

package org.redrune.network.rs666.packet.structure.out;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.Misc;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class LobbyResponseBuilder implements OutgoingPacketStructure {
	
	@Override
	public Packet build(Player player) {
		PacketBuilder builder = new PacketBuilder();
		for (int i = 1; i <= 5; i++) {
			builder.writeByte(0);
		}
		builder.writeLong(0);
		builder.writeByte(0);
		builder.writeInt(12);
		builder.writeByte(player.getCredentials().isDonator() ? 0x2 : 0);
		builder.writeInt(0);
		builder.writeByte(0);
		builder.writeInt(0);
		builder.writeShort(1); // recovery questions date set
		builder.writeShort(0); // unread messages
		long lastLogin = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1337);
		
		long now = System.currentTimeMillis();
		long jag = 1014753880308L;
		long since_jag = (now - jag) / 1000 / 60 / 60 / 24;
		long since_log = (now - lastLogin) / 1000 / 60 / 60 / 24;
		
		builder.writeShort((int) (since_jag - since_log)); // last logged in date
		builder.writeInt(Misc.IPAddressToNumber(Misc.formatIp(player.getNetworkSession().getChannel().getRemoteAddress().toString()))); // last login ip
		builder.writeByte(3); //email status (0 - no email, 1 - pending parental confirmation, 2 - pending confirmation, 3 - registered)
		builder.writeShort(0);
		builder.writeShort(0);
		builder.writeByte(0);
		builder.writeGJString2(player.getCredentials().getUsername());
		builder.writeByte(0);
		builder.writeInt(1);
		builder.writeByte(1);
		builder.writeShort(1); // current world id
		builder.writeGJString2("127.0.0.1");
		
		PacketBuilder lobbyResponse = new PacketBuilder();
		lobbyResponse.writeByte(builder.getBuffer().writerIndex());
		lobbyResponse.writeBytes(builder.getBuffer());
		return lobbyResponse.toPacket();
	}
}

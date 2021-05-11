package network.packet.outgoing.impl;

import game.entity.actor.player.Player;
import network.packet.Packet;
import network.packet.PacketBuilder;
import network.packet.PacketType;
import network.packet.outgoing.OutgoingPacketBuilder;
import utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-06
 */
public class MessagePacketBuilder extends OutgoingPacketBuilder {
	
	private final Player p;
	
	private final String text;
	
	private final int type;
	
	public MessagePacketBuilder(Player p, String text, int type) {
		super(new PacketBuilder(102, PacketType.VAR_BYTE));
		this.p = p;
		this.text = text;
		this.type = type;
	}
	
	@Override
	public Packet build() {
		int maskData = 0;
		if (p != null) {
			maskData |= 0x1;
			if (p.getAttributes().hasDisplayName()) {
				maskData |= 0x2;
			}
		}
		
		bldr.writeSmart(type);
		bldr.writeInt(0); // junk, not used by client
		bldr.writeByte(maskData);
		if ((maskData & 0x1) != 0) {
			bldr.writeString(p.getDisplayName());
			if (p.getAttributes().hasDisplayName()) {
				bldr.writeString(Misc.formatPlayerNameForDisplay(p.getUsername()));
			}
		}
		bldr.writeString(text);
		return bldr.toPacket();
	}
}

package org.redrune.networking.packet.outgoing.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.PacketBuilder;
import org.redrune.networking.packet.PacketType;
import org.redrune.networking.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.functions.Misc;

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
